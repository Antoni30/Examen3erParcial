package ec.edu.espe.inventario.Service;

import ec.edu.espe.inventario.DTO.CosechaDTO;
import ec.edu.espe.inventario.Entity.Cosecha;
import ec.edu.espe.inventario.Repository.CosechaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CosechaService {

    private final CosechaRepository cosechaRepository;

    public CosechaService(CosechaRepository cosechaRepository) {
        this.cosechaRepository = cosechaRepository;
    }

    public List<CosechaDTO> getAllCosechas() {
        return cosechaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CosechaDTO> getCosechaById(String id) {
        return cosechaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CosechaDTO createCosecha(CosechaDTO cosechaDTO) {
        Cosecha cosecha = convertToEntity(cosechaDTO);
        Cosecha savedCosecha = cosechaRepository.save(cosecha);
        return convertToDTO(savedCosecha);
    }

    public Optional<CosechaDTO> updateCosecha(String id, CosechaDTO cosechaDTO) {
        return cosechaRepository.findById(id)
                .map(existingCosecha -> {
                    existingCosecha.setCosecha_id(cosechaDTO.getCosecha_id());
                    return convertToDTO(cosechaRepository.save(existingCosecha));
                });
    }

    public boolean deleteCosecha(String id) {
        if (cosechaRepository.existsById(id)) {
            cosechaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CosechaDTO convertToDTO(Cosecha cosecha) {
        return new CosechaDTO(cosecha.getCosecha_id());
    }

    private Cosecha convertToEntity(CosechaDTO cosechaDTO) {
        return Cosecha.builder()
                .cosecha_id(cosechaDTO.getCosecha_id())
                .build();
    }
}