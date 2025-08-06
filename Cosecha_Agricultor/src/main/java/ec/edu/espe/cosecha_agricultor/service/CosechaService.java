package ec.edu.espe.cosecha_agricultor.service;

import ec.edu.espe.cosecha_agricultor.dto.CosechaDTO;
import ec.edu.espe.cosecha_agricultor.dto.EventoDTO;
import ec.edu.espe.cosecha_agricultor.model.Agricultor;
import ec.edu.espe.cosecha_agricultor.model.Cosecha;
import ec.edu.espe.cosecha_agricultor.repository.AgricultorRepository;
import ec.edu.espe.cosecha_agricultor.repository.CosechaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CosechaService {

    private final CosechaRepository cosechaRepository;
    private final AgricultorRepository agricultorRepository;
    private final EventoProducer eventoProducer;

    /**
     * Crea una nueva cosecha, la persiste y publica un evento "nueva_cosecha".
     */
    @Transactional
    public CosechaDTO crearCosecha(CosechaDTO dto) {
        Agricultor agricultor = agricultorRepository.findById(dto.getAgricultorId())
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        LocalDateTime fecha = dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDateTime.now();

        Cosecha cosecha = Cosecha.builder()
                .agricultor(agricultor)
                .producto(dto.getProducto())
                .toneladas(dto.getToneladas())
                .ubicacion(dto.getUbicacion())
                .fechaRegistro(fecha)
                .build();

        cosecha = cosechaRepository.save(cosecha);

        // Preparar y publicar evento - Corregido el constructor
        EventoDTO evento = new EventoDTO(
                "nueva_cosecha",
                cosecha.getId(),
                cosecha.getProducto(),
                cosecha.getToneladas(), // tonelada en EventoDTO
                1.0 // status
        );

        try {
            eventoProducer.enviarEventoNuevaCosecha(evento);
        } catch (Exception e) {
            System.err.println("Error al enviar evento: " + e.getMessage());
        }

        return mapToDTO(cosecha);
    }

    public List<CosechaDTO> obtenerTodas() {
        return cosechaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public CosechaDTO actualizarCosecha(UUID id, CosechaDTO dto) {
        Cosecha cosecha = cosechaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada"));

        cosecha.setProducto(dto.getProducto());
        cosecha.setToneladas(dto.getToneladas());
        cosecha.setUbicacion(dto.getUbicacion());

        cosecha = cosechaRepository.save(cosecha);
        return mapToDTO(cosecha);
    }

    public void eliminarCosecha(UUID id) {
        Cosecha cosecha = cosechaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada"));
        cosechaRepository.delete(cosecha);
    }

    // NUEVO MÉTODO: Actualizar estado de cosecha (para notificaciones de Facturación)
    @Transactional
    public CosechaDTO actualizarEstado(UUID id, String nuevoEstado, String facturaId) {
        Cosecha cosecha = cosechaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada con ID: " + id));

        // Actualizar el estado de la cosecha
        cosecha.setEstado(nuevoEstado);

        // Si se proporciona facturaId, también lo guardamos (opcional)
        if (facturaId != null && !facturaId.isEmpty()) {
            cosecha.setFacturaId(facturaId);
        }

        cosecha = cosechaRepository.save(cosecha);
        return mapToDTO(cosecha);
    }

    private CosechaDTO mapToDTO(Cosecha cosecha) {
        return CosechaDTO.builder()
                .id(cosecha.getId())
                .agricultorId(cosecha.getAgricultor().getId())
                .producto(cosecha.getProducto())
                .toneladas(cosecha.getToneladas())
                .ubicacion(cosecha.getUbicacion())
                .fechaRegistro(cosecha.getFechaRegistro())
                .build();
    }
}
