package ec.edu.espe.inventario.Service;

import ec.edu.espe.inventario.DTO.InsumoDTO;
import ec.edu.espe.inventario.Entity.Insumo;
import ec.edu.espe.inventario.Repository.InsumoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<InsumoDTO> getAllInsumos() {
        return insumoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<InsumoDTO> getInsumoById(UUID id) {
        return insumoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<InsumoDTO> getInsumosByIdCosecha(UUID idCosecha) {
        return insumoRepository.findByIdCosecha(idCosecha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InsumoDTO> getInsumosByNombre(String nombreInsumo) {
        return insumoRepository.findByNombreInsumoContaining(nombreInsumo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InsumoDTO createInsumo(InsumoDTO insumoDTO) {
        Insumo insumo = convertToEntity(insumoDTO);
        Insumo savedInsumo = insumoRepository.save(insumo);
        return convertToDTO(savedInsumo);
    }

    public Optional<InsumoDTO> updateInsumo(UUID id, InsumoDTO insumoDTO) {
        return insumoRepository.findById(id)
                .map(existingInsumo -> {
                    existingInsumo.setIdCosecha(insumoDTO.getIdCosecha());
                    existingInsumo.setNombreInsumo(insumoDTO.getNombreInsumo());
                    existingInsumo.setUnidadMedida(insumoDTO.getUnidadMedida());
                    existingInsumo.setCantidadDisponible(insumoDTO.getCantidadDisponible());
                    return convertToDTO(insumoRepository.save(existingInsumo));
                });
    }

    public boolean deleteInsumo(UUID id) {
        if (insumoRepository.existsById(id)) {
            insumoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // NUEVO MÉTODO: Descontar stock de insumos
    public boolean descontarStock(String nombreInsumo, double cantidadDescuento) {
        try {
            List<Insumo> insumos = insumoRepository.findByNombreInsumoContaining(nombreInsumo);

            if (insumos.isEmpty()) {
                return false; // No se encontró el insumo
            }

            // Buscar el insumo con suficiente stock
            for (Insumo insumo : insumos) {
                if (insumo.getCantidadDisponible() >= cantidadDescuento) {
                    insumo.setCantidadDisponible(insumo.getCantidadDisponible() - cantidadDescuento);
                    insumoRepository.save(insumo);
                    return true;
                }
            }

            return false; // No hay suficiente stock

        } catch (Exception e) {
            return false;
        }
    }

    private InsumoDTO convertToDTO(Insumo insumo) {
        return new InsumoDTO(
                insumo.getIdCosecha(),
                insumo.getNombreInsumo(),
                insumo.getUnidadMedida(),
                insumo.getCantidadDisponible()
        );
    }

    private Insumo convertToEntity(InsumoDTO insumoDTO) {
        return Insumo.builder()
                .idCosecha(insumoDTO.getIdCosecha())
                .nombreInsumo(insumoDTO.getNombreInsumo())
                .unidadMedida(insumoDTO.getUnidadMedida())
                .cantidadDisponible(insumoDTO.getCantidadDisponible())
                .build();
    }
}