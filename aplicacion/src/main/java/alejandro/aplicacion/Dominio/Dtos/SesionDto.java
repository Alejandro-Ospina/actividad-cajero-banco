package alejandro.aplicacion.Dominio.Dtos;

public record SesionDto(
        String token,
        String refreshToken
) {
}
