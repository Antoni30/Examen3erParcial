package ec.edu.espe.facturacion.listener;

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
    public void procesarEventoCosecha(String mensajeJson) {
        log.info("Mensaje JSON recibido en cola_facturacion: {}", mensajeJson);

        try {
            // Parsear manualmente el JSON para ser más robusto
            if (mensajeJson.contains("nueva_cosecha")) {
                log.info("Procesando evento de nueva cosecha desde JSON");

                // Extraer datos del JSON de forma simple
                String cosechaId = extraerValor(mensajeJson, "cosecha_id");
                String producto = extraerValor(mensajeJson, "producto");
                String toneladaStr = extraerValor(mensajeJson, "tonelada");

                if (cosechaId != null && producto != null && toneladaStr != null) {
                    double tonelada = Double.parseDouble(toneladaStr);

                    log.info("Datos extraídos - Cosecha: {}, Producto: {}, Toneladas: {}",
                            cosechaId, producto, tonelada);

                    // Crear EventoCosechaDTO
                    EventoCosechaDTO.PayloadDTO payload = EventoCosechaDTO.PayloadDTO.builder()
                            .cosechaId(cosechaId.replace("\"", ""))
                            .producto(producto.replace("\"", ""))
                            .toneladas(BigDecimal.valueOf(tonelada))
                            .build();

                    EventoCosechaDTO eventoConvertido = EventoCosechaDTO.builder()
                            .eventId(java.util.UUID.randomUUID().toString())
                            .eventType("nueva_cosecha")
                            .timestamp(LocalDateTime.now())
                            .payload(payload)
                            .build();

                    facturacionService.procesarNuevaCosecha(eventoConvertido);
                    log.info("Factura generada exitosamente para cosecha: {}", cosechaId);
                }
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de cosecha: {}", e.getMessage(), e);
        }
    }

    private String extraerValor(String json, String campo) {
        try {
            String patron = "\"" + campo + "\":";
            int inicio = json.indexOf(patron);
            if (inicio == -1) return null;

            inicio += patron.length();
            int fin = json.indexOf(",", inicio);
            if (fin == -1) fin = json.indexOf("}", inicio);

            return json.substring(inicio, fin).trim();
        } catch (Exception e) {
            log.error("Error extrayendo campo {}: {}", campo, e.getMessage());
            return null;
        }
    }
}
