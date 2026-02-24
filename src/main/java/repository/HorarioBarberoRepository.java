package repository;

import entity.HorarioBarbero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioBarberoRepository extends JpaRepository<HorarioBarbero, Long> {
    // Busca los horarios de un barbero en un día específico
    List<HorarioBarbero> findByBarberoIdAndDiaSemana(Long barberoId, String diaSemana);
}