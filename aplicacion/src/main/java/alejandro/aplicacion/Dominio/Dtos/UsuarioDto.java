package alejandro.aplicacion.Dominio.Dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        @NotEmpty
        String nombre,

        @NotEmpty
        String identificacion,

        @NotNull
        Integer edad,

        @NotEmpty
        String nacionalidad,

        @NotEmpty
        String genero,

        @Valid
        @NotNull
        CuentaBancoDto cuentaBanco
) {
}
