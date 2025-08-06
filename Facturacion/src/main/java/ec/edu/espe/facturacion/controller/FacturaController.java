package ec.edu.espe.facturacion.controller;

import ec.edu.espe.facturacion.dto.EventoCosechaDTO;
import ec.edu.espe.facturacion.dto.FacturaDTO;
import ec.edu.espe.facturacion.service.FacturacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FacturaController {

    private final FacturacionService facturacionService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> obtenerTodasLasFacturas() {
        try {
            List<FacturaDTO> facturas = facturacionService.obtenerTodasLasFacturas();
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            log.error("Error al obtener facturas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{facturaId}")
    public ResponseEntity<FacturaDTO> obtenerFacturaPorId(@PathVariable String facturaId) {
        try {
            Optional<FacturaDTO> factura = facturacionService.obtenerFacturaPorId(facturaId);
            return factura.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener factura {}: {}", facturaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cosecha/{cosechaId}")
    public ResponseEntity<FacturaDTO> obtenerFacturaPorCosechaId(@PathVariable String cosechaId) {
        try {
            Optional<FacturaDTO> factura = facturacionService.obtenerFacturaPorCosechaId(cosechaId);
            return factura.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener factura para cosecha {}: {}", cosechaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<FacturaDTO>> obtenerFacturasPendientes() {
        try {
            List<FacturaDTO> facturasPendientes = facturacionService.obtenerFacturasPendientes();
            return ResponseEntity.ok(facturasPendientes);
        } catch (Exception e) {
            log.error("Error al obtener facturas pendientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{facturaId}/pagar")
    public ResponseEntity<FacturaDTO> marcarComoPagada(@PathVariable String facturaId) {
        try {
            FacturaDTO facturaActualizada = facturacionService.marcarComoPagada(facturaId);
            return ResponseEntity.ok(facturaActualizada);
        } catch (RuntimeException e) {
            log.error("Error al marcar factura como pagada {}: {}", facturaId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al marcar factura como pagada {}: {}", facturaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/precios")
    public ResponseEntity<Map<String, BigDecimal>> obtenerPrecios() {
        try {
            Map<String, BigDecimal> precios = facturacionService.obtenerPrecios();
            return ResponseEntity.ok(precios);
        } catch (Exception e) {
            log.error("Error al obtener precios: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Facturacion",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }

    @PostMapping("/crear-prueba")
    public ResponseEntity<FacturaDTO> crearFacturaPrueba(
            @RequestParam String cosechaId,
            @RequestParam String producto,
            @RequestParam BigDecimal toneladas) {
        try {
            // Crear un evento de prueba
            EventoCosechaDTO evento = EventoCosechaDTO.builder()
                    .eventType("nueva_cosecha")
                    .payload(EventoCosechaDTO.PayloadDTO.builder()
                            .cosechaId(cosechaId)
                            .producto(producto)
                            .toneladas(toneladas)
                            .build())
                    .build();

            FacturaDTO factura = facturacionService.procesarNuevaCosecha(evento);
            log.info("Factura de prueba creada exitosamente: {}", factura.getFacturaId());
            return ResponseEntity.status(HttpStatus.CREATED).body(factura);
        } catch (Exception e) {
            log.error("Error al crear factura de prueba: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
