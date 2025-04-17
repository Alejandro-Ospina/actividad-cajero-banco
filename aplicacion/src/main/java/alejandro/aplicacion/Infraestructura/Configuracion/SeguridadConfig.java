package alejandro.aplicacion.Infraestructura.Configuracion;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Dominio.Dtos.FormatoRespuesta;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Infraestructura.Filtros.InterceptorDeEntrada;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SeguridadConfig {

    private final InterceptorDeEntrada filtro;

    @Bean
    public SecurityFilterChain configuracionSeguridad(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers(
                                        "/usuarios/crear-usuario",
                                        "/login",
                                        "/actualizar-token").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(error ->
                        error.authenticationEntryPoint((request,
                                                           response,
                                                           authException) ->
                                {
                                    respuesta(response, "Acceso no autorizado. Inicie sesión primero.");
                                }));

        return http.build();
    }

    private void respuesta(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper()
                .registerModules(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(
                        new FormatoRespuesta(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                mensaje
                        )
                )
        );
    }

    @Bean
    public AuthenticationManager administradorAutenticacion(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
