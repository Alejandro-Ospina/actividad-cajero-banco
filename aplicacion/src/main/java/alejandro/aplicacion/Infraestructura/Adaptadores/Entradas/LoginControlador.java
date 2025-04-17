package alejandro.aplicacion.Infraestructura.Adaptadores.Entradas;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Aplicacion.Servicios.RefreshTokenServicio;
import alejandro.aplicacion.Dominio.Dtos.LoginDto;
import alejandro.aplicacion.Dominio.Dtos.SesionDto;
import alejandro.aplicacion.Dominio.Dtos.TokensActualizacion;
import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginControlador {

    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final RefreshTokenServicio refreshTokenServicio;

    @PostMapping ("/login")
    public ResponseEntity<SesionDto> logeoUsuarios(@RequestBody @Valid LoginDto login){
        Authentication usuarioAutenticado = new UsernamePasswordAuthenticationToken(
                login.numeroCuenta(),
                login.clave()
        );

        var cuentaBancaria = manager.authenticate(usuarioAutenticado);
        var tokenUsuario = jwtService.generarToken((CuentaBanco) cuentaBancaria.getPrincipal());
        var refreshToken = jwtService.generarRefreshToken((CuentaBanco) cuentaBancaria.getPrincipal());
        refreshTokenServicio.eliminarRefreshToken(refreshToken);
        refreshTokenServicio.guardarRefreshToken(refreshToken);

        return ResponseEntity.ok(new SesionDto(tokenUsuario, refreshToken));
    }

    @PostMapping ("/actualizar-token")
    public ResponseEntity<TokensActualizacion> refreshToken(@RequestHeader("Authorization-Refresh") String refreshToken){
        var tokenActualizado = refreshTokenServicio.generarTokensActualizados(refreshToken);
        return ResponseEntity.ok(new TokensActualizacion(tokenActualizado));
    }
}
