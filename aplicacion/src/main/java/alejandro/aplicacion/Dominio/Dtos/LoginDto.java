package alejandro.aplicacion.Dominio.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull
        String numeroCuenta,

        @NotEmpty
        String clave
) {
}
