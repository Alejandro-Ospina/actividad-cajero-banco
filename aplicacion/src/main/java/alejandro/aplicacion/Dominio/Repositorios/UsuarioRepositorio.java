package alejandro.aplicacion.Dominio.Repositorios;

import alejandro.aplicacion.Dominio.Modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}
