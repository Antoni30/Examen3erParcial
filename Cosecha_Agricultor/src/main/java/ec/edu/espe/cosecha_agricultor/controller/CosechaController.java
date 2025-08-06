package ec.edu.espe.cosecha_agricultor.controller;

import ec.edu.espe.cosecha_agricultor.dto.CosechaDTO;
import ec.edu.espe.cosecha_agricultor.service.CosechaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cosechas")
@RequiredArgsConstructor
public class CosechaController {

    private final CosechaService cosechaService;

    @GetMapping
    public ResponseEntity<List<CosechaDTO>> listar() {
        return ResponseEntity.ok(cosechaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<CosechaDTO> crear(@Valid @RequestBody CosechaDTO dto) {
        CosechaDTO creado = cosechaService.crearCosecha(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CosechaDTO> actualizar(@PathVariable UUID id, @Valid @RequestBody CosechaDTO dto) {
        CosechaDTO actualizado = cosechaService.actualizarCosecha(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, String>> actualizarEstado(
            @PathVariable UUID id,
            @RequestBody Map<String, String> estadoRequest) {
        try {
            String nuevoEstado = estadoRequest.get("estado");
            String facturaId = estadoRequest.get("facturaId");

            CosechaDTO cosechaActualizada = cosechaService.actualizarEstado(id, nuevoEstado, facturaId);

            return ResponseEntity.ok(Map.of(
                    "message", "Estado actualizado correctamente",
                    "cosechaId", cosechaActualizada.getId().toString(),
                    "estado", nuevoEstado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar estado: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cosechaService.eliminarCosecha(id);
        return ResponseEntity.noContent().build();
    }
}
