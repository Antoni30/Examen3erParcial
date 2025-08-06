package ec.edu.espe.inventario.Controller;

import ec.edu.espe.inventario.DTO.EventoDTO;
import ec.edu.espe.inventario.Service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    @Autowired
    private EventoService eventoService;


    @GetMapping
    public ResponseEntity<List<EventoDTO>> getAllEventos() {
        List<EventoDTO> eventos = eventoService.getAllEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> getEventoById(@PathVariable Long id) {
        return eventoService.getEventoById(id)
                .map(evento -> ResponseEntity.ok(evento))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cosecha/{cosechaId}")
    public ResponseEntity<List<EventoDTO>> getEventosByCosechaId(@PathVariable String cosechaId) {
        List<EventoDTO> eventos = eventoService.getEventosByCosechaId(cosechaId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventoDTO>> getEventosByStatus(@PathVariable int status) {
        List<EventoDTO> eventos = eventoService.getEventosByStatus(status);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventoDTO>> getEventosByNombre(@RequestParam String evento) {
        List<EventoDTO> eventos = eventoService.getEventosByNombre(evento);
        return ResponseEntity.ok(eventos);
    }
}