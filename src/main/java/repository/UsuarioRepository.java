package repository;

import entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Magia de Spring: Busca por rol y que estén activos
    List<Usuario> findByRolAndActivoTrue(String rol);
}