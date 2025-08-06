package ec.edu.espe.inventario.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.inventario.DTO.EventoDTO;
import ec.edu.espe.inventario.Entity.Cosecha;
import ec.edu.espe.inventario.Entity.Evento;
import ec.edu.espe.inventario.Repository.EventoRepository;
import ec.edu.espe.inventario.Repository.CosechaRepository;
import org.slf4j.*;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventoConsumer.class);
    private final EventoRepository eventoRepository;
    private final CosechaRepository cosechaRepository;
    private final ObjectMapper objectMapper;

    public EventoConsumer(EventoRepository eventoRepository, CosechaRepository cosechaRepository, ObjectMapper objectMapper) {
        this.eventoRepository = eventoRepository;
        this.cosechaRepository = cosechaRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "inventario.cola")
    @Transactional
    public void receiveEventoMessage(String mensaje) {
        try {
            logger.info("Mensaje crudo recibido desde RabbitMQ: {}", mensaje);
            EventoDTO eventoDTO = objectMapper.readValue(mensaje, EventoDTO.class);
            logger.info("EventoDTO parseado: {}", eventoDTO.toString());
            
            // Guardar o verificar la cosecha
            saveCosechaIfNotExists(eventoDTO.getCosecha_id());
            
            // Guardar el evento
            Evento evento = convertToEntity(eventoDTO);
            Evento eventoGuardado = eventoRepository.save(evento);

            logger.info("Evento guardado exitosamente en la tabla eventos con ID: {}", eventoGuardado.getId_evento());
            logger.info("Detalles del evento: Evento={}, CosechaId={}, Producto={}, Toneladas={}, Status={}",
                    eventoGuardado.getEvento(),
                    eventoGuardado.getCosechaId(),
                    eventoGuardado.getProducto(),
                    eventoGuardado.getTonelada(),
                    eventoGuardado.getStatus());

        } catch (Exception e) {
            logger.error("Error procesando mensaje de RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el mensaje", e);
        }
    }

    private void saveCosechaIfNotExists(String cosechaId) {
        if (cosechaId != null && !cosechaRepository.existsById(cosechaId)) {
            Cosecha cosecha = Cosecha.builder()
                    .cosecha_id(cosechaId)
                    .build();
            Cosecha cosechaGuardada = cosechaRepository.save(cosecha);
            logger.info("Nueva cosecha guardada en la tabla cosechas: {}", cosechaGuardada.getCosecha_id());
        } else {
            logger.info("La cosecha {} ya existe en la tabla cosechas", cosechaId);
        }
    }

    private Evento convertToEntity(EventoDTO eventoDTO) {
        Evento evento = new Evento();
        evento.setEvento(eventoDTO.getEvento());
        evento.setCosechaId(eventoDTO.getCosecha_id());
        evento.setProducto(eventoDTO.getProducto());
        evento.setTonelada(eventoDTO.getTonelada());
        evento.setStatus(eventoDTO.getStatus());

        logger.debug("Convirtiendo EventoDTO a Evento entity: {}", evento);
        return evento;
    }
}
