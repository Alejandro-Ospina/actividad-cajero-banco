package alejandro.aplicacion.Infraestructura.Implementaciones.Servicios;

import alejandro.aplicacion.Aplicacion.Servicios.CuentaBancoServicio;
import alejandro.aplicacion.Aplicacion.Validadores.ValidarDatos;
import alejandro.aplicacion.Dominio.Dtos.*;
import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import alejandro.aplicacion.Dominio.Modelos.Transferencia;
import alejandro.aplicacion.Dominio.Modelos.Usuario;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Dominio.Repositorios.TransferenciaRepositorio;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.CuentaExistenteExcepcion;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.MontoNoPermitidoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaBancoServicioImpl implements CuentaBancoServicio {

    private final CuentaBancoRepositorio cuentaBancoRepositorio;
    private final ValidarDatos<UsuarioDto> validador;
    private final List<ValidarDatos<TransferirDineroDto>> validarDatos;
    private final PasswordEncoder encoder;
    private final TransferenciaRepositorio transferenciaRepositorio;

    @Override
    public MostrarCuentaBancoDto guardarCuentaBanco(UsuarioDto usuarioDto) {
        validador.validar(usuarioDto);
        CuentaBanco cuentaBanco = CuentaBancoDto.dtoToEntity(usuarioDto);
        cuentaBanco.setClave(encoder.encode(cuentaBanco.getClave()));
        cuentaBancoRepositorio.save(cuentaBanco);

        return new MostrarCuentaBancoDto(cuentaBanco);
    }

    @Override
    public void actualizarCuentaBanco() {

    }

    @Override
    @Transactional
    public MostrarTransferenciaDto transferirDinero(TransferirDineroDto dto, Authentication sesionUsuario) {
        validarDatos.forEach(validar -> validar.validar(dto));
        var usuarioAutenticado = (CuentaBanco) sesionUsuario.getPrincipal();

        if (usuarioAutenticado.getSaldo() < dto.monto())
            throw new MontoNoPermitidoException("El monto a transferir excede el saldo disponible");

        var cuentaEmisor = cuentaBancoRepositorio.findById(usuarioAutenticado.getId()).orElseThrow();
        var cuentaReceptor = cuentaBancoRepositorio.findByNumeroCuenta(dto.numeroCuentaReceptora()).orElseThrow();

        if (cuentaEmisor.getNumeroCuenta().equals(cuentaReceptor.getNumeroCuenta()))
            throw new RuntimeException("No se puede transferir a la cuenta origen.");

        cuentaEmisor.setSaldo(cuentaEmisor.getSaldo() - dto.monto());
        cuentaReceptor.setSaldo(cuentaReceptor.getSaldo() + dto.monto());

        Transferencia transferencia = new Transferencia(dto, cuentaEmisor, cuentaReceptor);
        transferenciaRepositorio.save(transferencia);

        return new MostrarTransferenciaDto(
                LocalDateTime.now(),
                cuentaEmisor.getUsuario().getNombre(),
                cuentaEmisor.getSaldo(),
                cuentaReceptor.getUsuario().getNombre(),
                cuentaReceptor.getNumeroCuenta().concat(" ").concat(cuentaReceptor.getTipoCuenta().name()),
                dto.monto()
        );
    }

    @Override
    public MostrarCuentaBancoDto crearNuevaCuenta(CuentaBancoDto cuentaBancoDto, Authentication usuarioAutenticado) {
        cuentaBancoRepositorio.findByNumeroCuenta(cuentaBancoDto.numeroCuenta()).ifPresent(
                error -> {throw new CuentaExistenteExcepcion("Ya existe el número de cuenta");
                }
        );

        var sesionUsuario = (CuentaBanco) usuarioAutenticado.getPrincipal();
        Usuario usuario = sesionUsuario.getUsuario();
        CuentaBanco nuevaCuenta = CuentaBancoDto.dtoToEntity(cuentaBancoDto, usuario);
        nuevaCuenta.setClave(encoder.encode(cuentaBancoDto.clave()));
        cuentaBancoRepositorio.save(nuevaCuenta);

        return new MostrarCuentaBancoDto(nuevaCuenta);
    }

    @Override
    public void retirarDinero() {

    }
}
