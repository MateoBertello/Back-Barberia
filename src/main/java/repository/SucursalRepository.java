package repository;

import entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {}