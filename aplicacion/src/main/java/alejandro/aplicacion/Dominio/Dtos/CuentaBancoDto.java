package alejandro.aplicacion.Dominio.Dtos;

import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import alejandro.aplicacion.Dominio.Modelos.Transferencia;
import alejandro.aplicacion.Dominio.Modelos.Usuario;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CuentaBancoDto(
        @NotEmpty
        String numeroCuenta,

        @NotEmpty
        String clave,

        @NotEmpty
        String nombreBanco,

        @NotEmpty
        String tipoCuenta,

        @NotNull
        Double saldo
) {
    public static CuentaBanco dtoToEntity(UsuarioDto usuarioDto){
        return CuentaBanco.builder()
                .saldo(usuarioDto.cuentaBanco().saldo())
                .clave(usuarioDto.cuentaBanco().clave())
                .nombreBanco(usuarioDto.cuentaBanco().nombreBanco())
                .numeroCuenta(usuarioDto.cuentaBanco().numeroCuenta())
                .tipoCuenta(CuentaBanco.TipoCuenta.valueOf(usuarioDto.cuentaBanco().tipoCuenta()))
                .usuario(Usuario.builder()
                        .edad(usuarioDto.edad())
                        .genero(usuarioDto.genero())
                        .identificacion(usuarioDto.identificacion())
                        .nacionalidad(usuarioDto.nacionalidad())
                        .nombre(usuarioDto.nombre())
                        .build()
                )
                .build();
    }

    public static CuentaBanco dtoToEntity(CuentaBancoDto dto, Usuario usuario){
        return CuentaBanco.builder()
                .usuario(usuario)
                .tipoCuenta(CuentaBanco.TipoCuenta.valueOf(dto.tipoCuenta))
                .numeroCuenta(dto.numeroCuenta)
                .nombreBanco(dto.nombreBanco)
                .clave(dto.clave)
                .saldo(dto.saldo)
                .build();
    }
}