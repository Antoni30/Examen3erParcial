package ec.edu.espe.facturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {

    private String facturaId;
    private String cosechaId;
    private BigDecimal monto;
    private Boolean pagado;
    private LocalDateTime fechaCreacion;
    private String producto;
    private BigDecimal toneladas;
}
