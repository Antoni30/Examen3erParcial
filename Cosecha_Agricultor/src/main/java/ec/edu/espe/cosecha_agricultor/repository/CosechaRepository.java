package ec.edu.espe.cosecha_agricultor.repository;

import ec.edu.espe.cosecha_agricultor.model.Cosecha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CosechaRepository extends JpaRepository<Cosecha, UUID> {
}
