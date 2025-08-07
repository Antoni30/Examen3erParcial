package ec.edu.espe.cosecha_agricultor.service;

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

    public void enviarEventoNuevaCosecha(EventoDTO eventoDTO) {
        try {
            // ENVIAR DIRECTAMENTE EL OBJETO: RabbitTemplate se encarga de la serialización
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NUEVA, eventoDTO);
            log.info("Evento enviado al exchange '{}' con routing key '{}' para cosecha: {}",
                    RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NUEVA, eventoDTO.getCosecha_id());

        } catch (Exception e) {
            log.error("Error al enviar el evento de nueva cosecha: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}