package ec.edu.espe.cosecha_agricultor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.cosecha_agricultor.config.RabbitMQConfig;
import ec.edu.espe.cosecha_agricultor.dto.EventoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void enviarEventoNuevaCosecha(EventoDTO eventoDTO) {
        try {
            String mensajeJson = objectMapper.writeValueAsString(eventoDTO);
            log.info("mensajeJson: {}", mensajeJson);
            rabbitTemplate.convertAndSend("inventario.cola", mensajeJson);
        } catch (Exception e) {
            log.error("Error al enviar el evento de nueva cosecha: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}