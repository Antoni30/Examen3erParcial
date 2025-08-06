package ec.edu.espe.cosecha_agricultor.repository;


import ec.edu.espe.cosecha_agricultor.model.Agricultor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AgricultorRepository extends JpaRepository<Agricultor, UUID> {
}
