package alejandro.aplicacion.Dominio.Modelos;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table (name = "cuenta_banco")
@EqualsAndHashCode (of = "id")
public class CuentaBanco implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (
            name = "numero_cuenta",
            unique = true
    )
    private String numeroCuenta;
    private String clave;

    @Column (name = "nombre_banco")
    private String nombreBanco;

    @Column (name = "tipo_cuenta")
    @Enumerated (EnumType.STRING)
    private TipoCuenta tipoCuenta;

    private Double saldo;

    @ManyToOne (
            fetch = FetchType.EAGER
    )
    @JoinColumn (
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    public enum TipoCuenta{
        AHORROS,
        CORRIENTE
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return numeroCuenta;
    }
}
