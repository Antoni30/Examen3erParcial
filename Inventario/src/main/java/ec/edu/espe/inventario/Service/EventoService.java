package ec.edu.espe.inventario.Service;

import ec.edu.espe.inventario.DTO.EventoDTO;
import ec.edu.espe.inventario.Entity.Evento;
import ec.edu.espe.inventario.Repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoService {

    private final EventoRepository eventoRepository;
    private final InsumoService insumoService;

    // NUEVO MÉTODO: Procesar evento de nueva cosecha
    public void procesarEventoNuevaCosecha(EventoDTO eventoDTO) {
        try {
            log.info("Procesando evento de nueva cosecha: {}", eventoDTO.getCosecha_id());

            // Calcular insumos necesarios según especificaciones
            double semillaNecesaria = eventoDTO.getTonelada() * 5; // 5kg semilla/tonelada
            double fertilizanteNecesario = eventoDTO.getTonelada() * 2; // 2kg fertilizante/tonelada

            log.info("Insumos calculados para cosecha {}: Semilla={}kg, Fertilizante={}kg",
                    eventoDTO.getCosecha_id(), semillaNecesaria, fertilizanteNecesario);

            // Actualizar stock de insumos
            actualizarStockInsumos(eventoDTO.getProducto(), semillaNecesaria, fertilizanteNecesario);

            // Guardar evento procesado
            guardarEvento(eventoDTO, 1); // status = 1 (procesado exitosamente)

            log.info("Evento de cosecha procesado exitosamente: {}", eventoDTO.getCosecha_id());

        } catch (Exception e) {
            log.error("Error al procesar evento de cosecha {}: {}", eventoDTO.getCosecha_id(), e.getMessage());

            // Guardar evento con error
            guardarEvento(eventoDTO, -1); // status = -1 (error)
            throw new RuntimeException("Error al procesar evento de cosecha", e);
        }
    }

    // NUEVO MÉTODO: Actualizar stock de insumos
    private void actualizarStockInsumos(String producto, double semillaNecesaria, double fertilizanteNecesario) {
        try {
            // Determinar tipo de semilla según el producto
            String tipoSemilla = determinarTipoSemilla(producto);

            // Actualizar stock de semilla
            boolean semillaActualizada = insumoService.descontarStock(tipoSemilla, semillaNecesaria);
            if (!semillaActualizada) {
                log.warn("No se pudo descontar stock suficiente de semilla: {}", tipoSemilla);
            }

            // Actualizar stock de fertilizante
            boolean fertilizanteActualizado = insumoService.descontarStock("Fertilizante N-PK", fertilizanteNecesario);
            if (!fertilizanteActualizado) {
                log.warn("No se pudo descontar stock suficiente de fertilizante N-PK");
            }

            log.info("Stock actualizado - Semilla: {} ({}kg), Fertilizante: N-PK ({}kg)",
                    tipoSemilla, semillaNecesaria, fertilizanteNecesario);

        } catch (Exception e) {
            log.error("Error al actualizar stock de insumos: {}", e.getMessage());
            throw e;
        }
    }

    // NUEVO MÉTODO: Determinar tipo de semilla según el producto
    private String determinarTipoSemilla(String producto) {
        if (producto.toLowerCase().contains("arroz")) {
            return "Semilla Arroz L-23";
        } else if (producto.toLowerCase().contains("café")) {
            return "Semilla Café Premium";
        } else if (producto.toLowerCase().contains("maíz")) {
            return "Semilla Maíz";
        } else {
            return "Semilla General"; // Fallback por defecto
        }
    }

    // NUEVO MÉTODO: Guardar evento en base de datos
    private void guardarEvento(EventoDTO eventoDTO, int status) {
        try {
            Evento evento = new Evento();
            evento.setEvento(eventoDTO.getEvento());
            evento.setCosechaId(eventoDTO.getCosecha_id());
            evento.setProducto(eventoDTO.getProducto());
            evento.setTonelada(eventoDTO.getTonelada());
            evento.setStatus(status);

            eventoRepository.save(evento);
            log.debug("Evento guardado en base de datos: {}", eventoDTO.getCosecha_id());

        } catch (Exception e) {
            log.error("Error al guardar evento en base de datos: {}", e.getMessage());
        }
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