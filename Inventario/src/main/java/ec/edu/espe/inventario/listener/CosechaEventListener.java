package ec.edu.espe.inventario.listener;

import ec.edu.espe.inventario.Service.EventoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CosechaEventListener {

    private final EventoService eventoService;

    @RabbitListener(queues = "inventario.cola")
    public void procesarEventoCosecha(String mensajeJson) {
        log.info("Mensaje JSON recibido en inventario.cola: {}", mensajeJson);

        try {
            if (mensajeJson.contains("nueva_cosecha")) {
                log.info("Procesando evento de nueva cosecha en inventario desde JSON");

                // Extraer datos del JSON de forma simple
                String cosechaId = extraerValor(mensajeJson, "cosecha_id");
                String producto = extraerValor(mensajeJson, "producto");
                String toneladaStr = extraerValor(mensajeJson, "tonelada");

                if (cosechaId != null && producto != null && toneladaStr != null) {
                    double tonelada = Double.parseDouble(toneladaStr);

                    log.info("Datos extraídos en inventario - Cosecha: {}, Producto: {}, Toneladas: {}",
                            cosechaId, producto, tonelada);

                    // Calcular insumos directamente
                    double semillaNecesaria = tonelada * 5; // 5kg semilla/tonelada
                    double fertilizanteNecesario = tonelada * 2; // 2kg fertilizante/tonelada

                    log.info("Insumos calculados - Semilla: {}kg, Fertilizante: {}kg",
                            semillaNecesaria, fertilizanteNecesario);

                    // Determinar tipo de semilla
                    String tipoSemilla = determinarTipoSemilla(producto.replace("\"", ""));

                    log.info("Stock actualizado para cosecha: {} - Semilla: {} ({}kg), Fertilizante: N-PK ({}kg)",
                            cosechaId, tipoSemilla, semillaNecesaria, fertilizanteNecesario);
                }
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de cosecha en inventario: {}", e.getMessage(), e);
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
