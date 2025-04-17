package alejandro.aplicacion.Dominio.Repositorios;

import alejandro.aplicacion.Dominio.Modelos.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransferenciaRepositorio extends JpaRepository<Transferencia, Long> {

    Page<Transferencia> findAllByCuentaEmisor(String numeroCuentas, Pageable pageable);
}
