package ec.edu.espe.cosecha_agricultor.controller;

import ec.edu.espe.cosecha_agricultor.dto.CosechaDTO;
import ec.edu.espe.cosecha_agricultor.service.CosechaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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
}
