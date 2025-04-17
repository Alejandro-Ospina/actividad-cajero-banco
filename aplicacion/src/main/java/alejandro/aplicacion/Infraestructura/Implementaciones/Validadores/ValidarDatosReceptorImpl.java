package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores;

import alejandro.aplicacion.Aplicacion.Validadores.ValidarDatos;
import alejandro.aplicacion.Dominio.Dtos.TransferirDineroDto;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.EntradaNoCoincidenteExcepcion;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarDatosReceptorImpl implements ValidarDatos<TransferirDineroDto> {

    private final CuentaBancoRepositorio repositorio;

    @Override
    public void validar(TransferirDineroDto datos) {
        var cuentaBancoReceptora = repositorio.findByNumeroCuenta(datos.numeroCuentaReceptora()).orElseThrow(
                () -> new EntityNotFoundException("La cuenta receptora no se encuentra registrada")
        );

        if (!datos.nombreBanco().equals(cuentaBancoReceptora.getNombreBanco()))
            throw new EntradaNoCoincidenteExcepcion("El nombre de la cuenta de banco no coincide con la registrada.");

        if (!datos.tipoCuenta().equals(cuentaBancoReceptora.getTipoCuenta().name()))
            throw new EntradaNoCoincidenteExcepcion("El tipo de cuenta de banco no coincide con la registrada");
    }
}
