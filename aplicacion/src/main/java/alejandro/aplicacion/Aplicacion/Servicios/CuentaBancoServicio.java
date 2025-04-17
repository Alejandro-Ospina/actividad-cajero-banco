package alejandro.aplicacion.Aplicacion.Servicios;

import alejandro.aplicacion.Dominio.Dtos.*;
import org.springframework.security.core.Authentication;

public interface CuentaBancoServicio {

    MostrarCuentaBancoDto guardarCuentaBanco(UsuarioDto usuarioDto);
    void actualizarCuentaBanco();
    MostrarTransferenciaDto transferirDinero(TransferirDineroDto dto, Authentication sesionUsuario);
    MostrarCuentaBancoDto crearNuevaCuenta(CuentaBancoDto cuentaBancoDto, Authentication usuarioAutenticado);
    void retirarDinero();
}
