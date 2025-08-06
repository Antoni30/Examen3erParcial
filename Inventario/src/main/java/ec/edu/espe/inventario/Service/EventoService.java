package ec.edu.espe.inventario.Service;

import ec.edu.espe.inventario.DTO.EventoDTO;
import ec.edu.espe.inventario.Entity.Evento;
import ec.edu.espe.inventario.Repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<EventoDTO> getAllEventos() {
        return eventoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventoDTO> getEventoById(Long id) {
        return eventoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<EventoDTO> getEventosByCosechaId(String cosechaId) {
        return eventoRepository.findByCosechaId(cosechaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventoDTO> getEventosByStatus(int status) {
        return eventoRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventoDTO> getEventosByNombre(String evento) {
        return eventoRepository.findByEventoContaining(evento).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EventoDTO convertToDTO(Evento evento) {
        return new EventoDTO(
                evento.getEvento(),
                evento.getCosechaId(),
                evento.getProducto(),
                evento.getTonelada(),
                evento.getStatus()
        );
    }
}