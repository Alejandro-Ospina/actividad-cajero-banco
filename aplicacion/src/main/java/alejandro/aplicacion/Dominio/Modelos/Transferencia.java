package alejandro.aplicacion.Dominio.Modelos;

import alejandro.aplicacion.Dominio.Dtos.TransferirDineroDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "transferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode (of = "id")
public class Transferencia {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Column (name = "emisor")
    private String nombreEmisor;

    @Column (name = "cuenta_emisor")
    private String cuentaEmisor;

    @Column (name = "identificacion")
    private String identificacionEmisor;

    @Column (name = "receptor")
    private String cuentaReceptor;

    @Column (name = "nombre_receptor")
    private String nombreReceptor;

    @Column (name = "tipo")
    private String tipoCuenta;
    private Double monto;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (
            name = "cuenta_id",
            nullable = false
    )
    private CuentaBanco cuentaBanco;

    public Transferencia(TransferirDineroDto dto, CuentaBanco cuentaBancoEmisor, CuentaBanco cuentaBancoReceptor){
        this.fecha = LocalDateTime.now();
        this.nombreEmisor = cuentaBancoEmisor.getUsuario().getNombre();
        this.identificacionEmisor = cuentaBancoEmisor.getUsuario().getIdentificacion();
        this.cuentaReceptor = cuentaBancoReceptor.getNumeroCuenta();
        this.nombreReceptor = cuentaBancoReceptor.getUsuario().getNombre();
        this.tipoCuenta = dto.tipoCuenta();
        this.monto = dto.monto();
        this.cuentaBanco = cuentaBancoEmisor;
        this.cuentaEmisor = cuentaBancoEmisor.getNumeroCuenta();
    }
}
