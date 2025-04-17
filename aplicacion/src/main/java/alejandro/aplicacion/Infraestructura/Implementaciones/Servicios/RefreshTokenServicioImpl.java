package alejandro.aplicacion.Infraestructura.Implementaciones.Servicios;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Aplicacion.Servicios.RefreshTokenServicio;
import alejandro.aplicacion.Aplicacion.Validadores.ValidarDatos;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.EntradaNoCoincidenteExcepcion;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenServicioImpl implements RefreshTokenServicio {

    private final ValidarDatos<String> validarDatos;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;
    private final CuentaBancoRepositorio repositorio;

    @Value("${expiracion.refresh.token}")
    private int duracionRefresh;

    @Override
    public void guardarRefreshToken(String refreshToken) {

        validarDatos.validar(refreshToken);
        if (!jwtService.esValidoElToken(refreshToken))
            throw new RuntimeException("El refresh token no es válido");

        redisTemplate.opsForValue().set(
                jwtService.obtenerCuentaBancaria(refreshToken),
                refreshToken,
                duracionRefresh,
                TimeUnit.SECONDS);
    }

    @Override
    public void eliminarRefreshToken(String refreshToken) {
        var identificador = jwtService.obtenerCuentaBancaria(refreshToken);
        if (redisTemplate.hasKey(identificador))
            redisTemplate.delete(identificador);
    }

    @Override
    public String generarTokensActualizados(String refreshToken) {
        var identificador = jwtService.obtenerCuentaBancaria(refreshToken);

        if (!redisTemplate.hasKey(identificador))
            throw new EntradaNoCoincidenteExcepcion("No se encontró un refresh token asociado al usuario");

        var cuentaBancaria = repositorio.findByNumeroCuenta(jwtService.obtenerCuentaBancaria(refreshToken)).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado una cuenta de banco asociada al token de refresco")
        );

        return jwtService.generarToken(cuentaBancaria);
    }
}
