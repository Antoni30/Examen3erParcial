package ec.edu.espe.inventario.Repository;

import ec.edu.espe.inventario.Entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCosechaId(String cosechaId);
    List<Evento> findByStatus(int status);
    List<Evento> findByEventoContaining(String evento);
}