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
public class EventoCosechaDTO {

    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private PayloadDTO payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayloadDTO {
        private String cosechaId;
        private String producto;
        private BigDecimal toneladas;
        private String[] requiereInsumos;
    }
}
