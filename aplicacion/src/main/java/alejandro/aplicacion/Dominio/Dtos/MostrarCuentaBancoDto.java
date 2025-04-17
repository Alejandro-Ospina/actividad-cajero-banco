package alejandro.aplicacion.Dominio.Dtos;

import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;

public record MostrarCuentaBancoDto(
        Integer id,
        String numeroCuenta,
        String nombreBanco,
        String tipoCuenta,
        Double saldo,
        MostrarUsuarioDto usuarioDto
) {
    public MostrarCuentaBancoDto(CuentaBanco cuentaBanco){
        this(
                cuentaBanco.getId(),
                cuentaBanco.getNumeroCuenta(),
                cuentaBanco.getNombreBanco(),
                cuentaBanco.getTipoCuenta().name(),
                cuentaBanco.getSaldo(),
                new MostrarUsuarioDto(cuentaBanco.getUsuario())
        );
    }
}
