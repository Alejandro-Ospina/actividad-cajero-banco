package alejandro.aplicacion.Aplicacion.Servicios;

public interface RefreshTokenServicio {

    void guardarRefreshToken(String refreshToken);
    void eliminarRefreshToken(String refreshToken);
    String generarTokensActualizados(String refreshToken);
}
