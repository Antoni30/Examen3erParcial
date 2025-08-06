package ec.edu.espe.inventario.Controller;

import ec.edu.espe.inventario.DTO.InsumoDTO;
import ec.edu.espe.inventario.Service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {
    @Autowired
    private  InsumoService insumoService;

    @GetMapping
    public ResponseEntity<List<InsumoDTO>> getAllInsumos() {
        List<InsumoDTO> insumos = insumoService.getAllInsumos();
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoDTO> getInsumoById(@PathVariable UUID id) {
        return insumoService.getInsumoById(id)
                .map(insumo -> ResponseEntity.ok(insumo))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cosecha/{idCosecha}")
    public ResponseEntity<List<InsumoDTO>> getInsumosByIdCosecha(@PathVariable UUID idCosecha) {
        List<InsumoDTO> insumos = insumoService.getInsumosByIdCosecha(idCosecha);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InsumoDTO>> getInsumosByNombre(@RequestParam String nombre) {
        List<InsumoDTO> insumos = insumoService.getInsumosByNombre(nombre);
        return ResponseEntity.ok(insumos);
    }

    @PostMapping
    public ResponseEntity<InsumoDTO> createInsumo(@RequestBody InsumoDTO insumoDTO) {
        InsumoDTO createdInsumo = insumoService.createInsumo(insumoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInsumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumoDTO> updateInsumo(@PathVariable UUID id, @RequestBody InsumoDTO insumoDTO) {
        return insumoService.updateInsumo(id, insumoDTO)
                .map(updatedInsumo -> ResponseEntity.ok(updatedInsumo))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsumo(@PathVariable UUID id) {
        if (insumoService.deleteInsumo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}