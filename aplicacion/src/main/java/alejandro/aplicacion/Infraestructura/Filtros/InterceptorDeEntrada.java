package alejandro.aplicacion.Infraestructura.Filtros;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Dominio.Dtos.FormatoRespuesta;
import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InterceptorDeEntrada extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CuentaBancoRepositorio repositorio;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String headerRefresh = request.getHeader("Authorization-Refresh");

        if (headerRefresh != null){

            if (!jwtService.esValidoElToken(headerRefresh) && jwtService.obtenerTipo(headerRefresh).equals("REFRESH")){
                manejoError(response, "Refresh token caducado o no válido, inicie sesión de nuevo");
                return;
            }

            var identificador = jwtService.obtenerCuentaBancaria(headerRefresh);
            if (!redisTemplate.hasKey(identificador)){
                manejoError(response, "No existe un refresh token asociado al usuario. Inicie sesión de nuevo.");
                return;
            }

            if (redisTemplate.hasKey(identificador)){
                var token = redisTemplate.opsForValue().get(identificador);
                if (token != null && !token.equals(headerRefresh)){
                    manejoError(response, "Los tokens de refresco no coinciden. Inicie sesión de nuevo.");
                    return;
                }
            }
        }

        if (header != null) {

            String token = header.substring(7);
            if (jwtService.esValidoElToken(token) && jwtService.obtenerTipo(token) == null){

                String identificacion = jwtService.obtenerSujeto(token);
                CuentaBanco usuarioAutenticado = repositorio.findByNumeroCuenta(jwtService.obtenerCuentaBancaria(token)).orElse(null);
                if (usuarioAutenticado != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    Authentication autenticacion = new UsernamePasswordAuthenticationToken(
                            usuarioAutenticado,
                            null,
                            usuarioAutenticado.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }else{
                    manejoError(response, "Usuario no encontrado. Por favor registrese!");
                    return;
                }
            }else{
                manejoError(response, "Token inválido o de refresco, verifique su identidad");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void manejoError(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper()
                        .registerModules(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(
                                new FormatoRespuesta(
                                        LocalDateTime.now(),
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        mensaje
                                )
                        )
        );
    }
}
