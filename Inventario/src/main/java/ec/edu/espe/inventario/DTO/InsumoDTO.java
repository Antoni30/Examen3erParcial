package ec.edu.espe.inventario.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoDTO {
    private UUID idCosecha;
    private String nombreInsumo;
    private String unidadMedida;
    private Double cantidadDisponible;
}