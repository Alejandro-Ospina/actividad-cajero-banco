package alejandro.aplicacion.Dominio.Repositorios;

import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaBancoRepositorio extends JpaRepository<CuentaBanco, Integer> {

    @Query ("""
            SELECT c
            FROM CuentaBanco c
            WHERE c.numeroCuenta = ?1
            AND c.usuario.identificacion = ?2
            """)
    Optional<CuentaBanco> getCuentaBancoByNumeroCuentaAndIdentificacion(String numeroCuenta, String identificacion);

    Optional<CuentaBanco> findByNumeroCuenta(String numeroCuenta);

    @Query(
            """
            SELECT c
            FROM CuentaBanco c
            WHERE c.usuario.identificacion = :identificacion
            """)
    Optional<CuentaBanco> encontrarUsuarioPorIdentificacion(String identificacion);
}
