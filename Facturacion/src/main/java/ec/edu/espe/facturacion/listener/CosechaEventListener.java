package ec.edu.espe.facturacion.listener;

import ec.edu.espe.facturacion.dto.EventoDTO;
import ec.edu.espe.facturacion.dto.EventoCosechaDTO;
import ec.edu.espe.facturacion.service.FacturacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CosechaEventListener {

    private final FacturacionService facturacionService;

    @RabbitListener(queues = "cola_facturacion")
    public void procesarEventoCosecha(EventoDTO evento) {
        log.info("Evento recibido en cola_facturacion: {}", evento.getEvento());

        try {
            if ("nueva_cosecha".equals(evento.getEvento())) {
                log.info("Procesando nueva cosecha: {} - {} toneladas de {}",
                        evento.getCosecha_id(),
                        evento.getTonelada(),
                        evento.getProducto());

                // Crear EventoCosechaDTO
                EventoCosechaDTO.PayloadDTO payload = EventoCosechaDTO.PayloadDTO.builder()
                        .cosechaId(evento.getCosecha_id().toString())
                        .producto(evento.getProducto())
                        .toneladas(BigDecimal.valueOf(evento.getTonelada()))
                        .build();

                EventoCosechaDTO eventoConvertido = EventoCosechaDTO.builder()
                        .eventId(java.util.UUID.randomUUID().toString())
                        .eventType("nueva_cosecha")
                        .timestamp(LocalDateTime.now())
                        .payload(payload)
                        .build();

                facturacionService.procesarNuevaCosecha(eventoConvertido);
                log.info("Factura generada exitosamente para cosecha: {}", evento.getCosecha_id());
            } else {
                log.info("Tipo de evento no manejado: {}", evento.getEvento());
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de cosecha: {}", e.getMessage(), e);
        }
    }
}
