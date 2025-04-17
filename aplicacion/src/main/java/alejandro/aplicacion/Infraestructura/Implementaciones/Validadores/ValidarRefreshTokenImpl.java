package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Aplicacion.Validadores.ValidarDatos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarRefreshTokenImpl implements ValidarDatos<String> {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    @Override
    public void validar(String refreshToken) {
        var identificador = jwtService.obtenerCuentaBancaria(refreshToken);

        if (redisTemplate.hasKey(identificador)) return;
    }
}
