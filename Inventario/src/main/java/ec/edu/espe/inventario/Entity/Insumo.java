package ec.edu.espe.inventario.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue
    @Column(name = "insumo_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "id_cosecha", nullable = false, length = 100)
    private UUID idCosecha ;

    @Column(name = "nombre_insumo", nullable = false, length = 100)
    private String nombreInsumo;

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida; // Ej: kg, litros

    @Column(name = "cantidad_disponible", nullable = false)
    private Double cantidadDisponible;

}
