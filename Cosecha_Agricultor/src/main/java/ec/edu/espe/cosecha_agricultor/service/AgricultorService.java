package ec.edu.espe.cosecha_agricultor.service;

import ec.edu.espe.cosecha_agricultor.dto.AgricultorDTO;
import ec.edu.espe.cosecha_agricultor.model.Agricultor;
import ec.edu.espe.cosecha_agricultor.repository.AgricultorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgricultorService {

    private final AgricultorRepository agricultorRepository;

    public AgricultorDTO crearAgricultor(AgricultorDTO dto) {
        Agricultor agricultor = Agricultor.builder()
                .nombre(dto.getNombre())
                .build();

        agricultor = agricultorRepository.save(agricultor);

        return mapToDTO(agricultor);
    }

    public List<AgricultorDTO> obtenerTodos() {
        return agricultorRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AgricultorDTO mapToDTO(Agricultor agricultor) {
        return AgricultorDTO.builder()
                .id(agricultor.getId())
                .nombre(agricultor.getNombre())
                .build();
    }
}
