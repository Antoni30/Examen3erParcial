package ec.edu.espe.inventario.Repository;

import ec.edu.espe.inventario.Entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, UUID> {
    List<Insumo> findByIdCosecha(UUID idCosecha);
    List<Insumo> findByNombreInsumoContaining(String nombreInsumo);
}