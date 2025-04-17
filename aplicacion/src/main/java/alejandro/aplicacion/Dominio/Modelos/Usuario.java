package alejandro.aplicacion.Dominio.Modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table (name = "usuario")
@EqualsAndHashCode (of = "id")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (unique = true)
    private String nombre;

    @Column (unique = true)
    private String identificacion;

    private int edad;
    private String nacionalidad;
    private String genero;
}
