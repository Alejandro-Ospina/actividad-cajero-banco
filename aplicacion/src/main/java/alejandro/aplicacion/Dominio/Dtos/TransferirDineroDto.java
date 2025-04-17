package alejandro.aplicacion.Dominio.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferirDineroDto(
        @NotNull
        @Positive
        Double monto,

        @NotEmpty
        String numeroCuentaReceptora,

        @NotEmpty
        String nombreBanco,

        @NotEmpty
        String tipoCuenta
) {
}
