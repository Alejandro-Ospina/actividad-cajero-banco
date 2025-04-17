package alejandro.aplicacion.Aplicacion.Servicios;

import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;

public interface JwtService {

    String generarToken(CuentaBanco cuentaBanco);
    String generarRefreshToken(CuentaBanco cuentaBanco);
    boolean esValidoElToken(String token);
    String obtenerSujeto(String token);
    String obtenerTipo(String token);
    String obtenerCuentaBancaria(String refreshToken);
}
