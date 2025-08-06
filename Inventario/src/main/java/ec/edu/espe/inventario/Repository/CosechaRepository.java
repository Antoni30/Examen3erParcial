package ec.edu.espe.inventario.Repository;

import ec.edu.espe.inventario.Entity.Cosecha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CosechaRepository extends JpaRepository<Cosecha, String> {
}