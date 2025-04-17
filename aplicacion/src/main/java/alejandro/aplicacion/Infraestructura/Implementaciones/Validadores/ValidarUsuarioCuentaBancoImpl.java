package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores;

import alejandro.aplicacion.Aplicacion.Validadores.ValidarDatos;
import alejandro.aplicacion.Dominio.Dtos.UsuarioDto;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.CuentaExistenteExcepcion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarUsuarioCuentaBancoImpl implements ValidarDatos<UsuarioDto> {

    private final CuentaBancoRepositorio cuentaBancoRepositorio;

    @Override
    public void validar(UsuarioDto usuarioDto) throws CuentaExistenteExcepcion{
        cuentaBancoRepositorio.getCuentaBancoByNumeroCuentaAndIdentificacion(
                usuarioDto.cuentaBanco().numeroCuenta(),
                usuarioDto.identificacion()
        ).ifPresent(
                error -> {
                    throw new CuentaExistenteExcepcion("Ya existe un usuario con el numero de cuenta e identificacion dados");
                }
        );
    }
}
