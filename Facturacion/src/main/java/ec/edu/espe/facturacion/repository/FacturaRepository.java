package ec.edu.espe.facturacion.repository;

import ec.edu.espe.facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, String> {

    Optional<Factura> findByCosechaId(String cosechaId);

    List<Factura> findByPagado(Boolean pagado);

    @Query("SELECT f FROM Factura f WHERE f.producto = :producto")
    List<Factura> findByProducto(@Param("producto") String producto);

    @Query("SELECT COUNT(f) FROM Factura f WHERE f.pagado = false")
    long countFacturasPendientes();
}
