package ec.edu.espe.inventario.Controller;

import ec.edu.espe.inventario.DTO.CosechaDTO;
import ec.edu.espe.inventario.Service.CosechaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cosechas")
public class CosechaController {

    @Autowired
    private CosechaService cosechaService;

    @GetMapping
    public ResponseEntity<List<CosechaDTO>> getAllCosechas() {
        List<CosechaDTO> cosechas = cosechaService.getAllCosechas();
        return ResponseEntity.ok(cosechas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CosechaDTO> getCosechaById(@PathVariable String id) {
        return cosechaService.getCosechaById(id)
                .map(cosecha -> ResponseEntity.ok(cosecha))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CosechaDTO> createCosecha(@RequestBody CosechaDTO cosechaDTO) {
        CosechaDTO createdCosecha = cosechaService.createCosecha(cosechaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCosecha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CosechaDTO> updateCosecha(@PathVariable String id, @RequestBody CosechaDTO cosechaDTO) {
        return cosechaService.updateCosecha(id, cosechaDTO)
                .map(updatedCosecha -> ResponseEntity.ok(updatedCosecha))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCosecha(@PathVariable String id) {
        if (cosechaService.deleteCosecha(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}