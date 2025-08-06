package ec.edu.espe.cosecha_agricultor.controller;

import ec.edu.espe.cosecha_agricultor.dto.AgricultorDTO;
import ec.edu.espe.cosecha_agricultor.service.AgricultorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/agricultores")
@RequiredArgsConstructor
public class AgricultorController {

    private final AgricultorService agricultorService;

    @GetMapping
    public ResponseEntity<List<AgricultorDTO>> listar() {
        List<AgricultorDTO> lista = agricultorService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<AgricultorDTO> crear(@Valid @RequestBody AgricultorDTO dto) {
        AgricultorDTO creado = agricultorService.crearAgricultor(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }
}
