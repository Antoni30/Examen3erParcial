package ec.edu.espe.cosecha_agricultor.service;

import ec.edu.espe.cosecha_agricultor.dto.CosechaDTO;
import ec.edu.espe.cosecha_agricultor.model.Agricultor;
import ec.edu.espe.cosecha_agricultor.model.Cosecha;
import ec.edu.espe.cosecha_agricultor.repository.AgricultorRepository;
import ec.edu.espe.cosecha_agricultor.repository.CosechaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CosechaService {

    private final CosechaRepository cosechaRepository;
    private final AgricultorRepository agricultorRepository;

    public CosechaDTO crearCosecha(CosechaDTO dto) {
        Agricultor agricultor = agricultorRepository.findById(dto.getAgricultorId())
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        Cosecha cosecha = Cosecha.builder()
                .agricultor(agricultor)
                .producto(dto.getProducto())
                .toneladas(dto.getToneladas())
                .ubicacion(dto.getUbicacion())
                .fechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDateTime.now())
                .build();

        cosecha = cosechaRepository.save(cosecha);

        return mapToDTO(cosecha);
    }

    public List<CosechaDTO> obtenerTodas() {
        return cosechaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
