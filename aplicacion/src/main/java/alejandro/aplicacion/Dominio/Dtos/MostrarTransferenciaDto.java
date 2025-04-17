package alejandro.aplicacion.Dominio.Dtos;

import alejandro.aplicacion.Dominio.Modelos.Transferencia;

import java.time.LocalDateTime;

public record MostrarTransferenciaDto(
        LocalDateTime fecha,
        String emisor,
        Double saldo,
        String destinatario,
        String cuenta,
        Double montoTransferido
) {
    public MostrarTransferenciaDto(Transferencia transferencia){
        this(
                transferencia.getFecha(),
                transferencia.getNombreEmisor(),
                transferencia.getCuentaBanco().getSaldo(),
                transferencia.getNombreReceptor(),
                transferencia.getCuentaReceptor(),
                transferencia.getMonto()
        );
    }
}
