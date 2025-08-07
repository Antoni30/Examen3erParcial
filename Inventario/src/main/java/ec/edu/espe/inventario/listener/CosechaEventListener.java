package ec.edu.espe.inventario.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class CosechaEventListener {

    @RabbitListener(queues = "inventario.cola")
    public void procesarEventoCosecha(String mensajeJson) {
        log.info("Mensaje JSON recibido en inventario.cola: {}", mensajeJson);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode eventoNode = objectMapper.readTree(mensajeJson);

            String evento = eventoNode.get("evento").asText();
            if ("nueva_cosecha".equals(evento)) {
                String cosechaId = eventoNode.get("cosecha_id").asText();
                String producto = eventoNode.get("producto").asText();
                double tonelada = eventoNode.get("tonelada").asDouble();

                log.info("Procesando nueva cosecha en inventario: {} - {} toneladas de {}",
                        cosechaId, tonelada, producto);

                // Calcular insumos necesarios según especificaciones
                double semillaNecesaria = tonelada * 5; // 5kg semilla/tonelada
                double fertilizanteNecesario = tonelada * 2; // 2kg fertilizante/tonelada

                log.info("Insumos calculados - Semilla: {}kg, Fertilizante: {}kg",
                        semillaNecesaria, fertilizanteNecesario);

                // Determinar tipo de semilla según el producto
                String tipoSemilla = determinarTipoSemilla(producto);

                log.info("Stock actualizado para cosecha: {} - Semilla: {} ({}kg), Fertilizante: N-PK ({}kg)",
                        cosechaId, tipoSemilla, semillaNecesaria, fertilizanteNecesario);
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de cosecha en inventario: {}", e.getMessage(), e);
        }
    }

    private String determinarTipoSemilla(String producto) {
        if (producto.toLowerCase().contains("arroz")) {
            return "Semilla Arroz L-23";
        } else if (producto.toLowerCase().contains("café")) {
            return "Semilla Café Premium";
        } else if (producto.toLowerCase().contains("maíz")) {
            return "Semilla Maíz";
        } else {
            return "Semilla General";
        }
    }
}
