package ec.edu.espe.facturacion.service;

import ec.edu.espe.facturacion.dto.ActualizarEstadoCosechaDTO;
import ec.edu.espe.facturacion.dto.EventoCosechaDTO;
import ec.edu.espe.facturacion.dto.FacturaDTO;
import ec.edu.espe.facturacion.model.Factura;
import ec.edu.espe.facturacion.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacturacionService {

    private final FacturaRepository facturaRepository;
    private final RestTemplate restTemplate;

    @Value("${microservicio.central.url:http://localhost:8080}")
    private String centralServiceUrl;

    // Precios de referencia configurables (USD/tonelada)
    private static final Map<String, BigDecimal> PRECIOS = new HashMap<>();

    static {
        PRECIOS.put("Arroz Oro", BigDecimal.valueOf(120));
        PRECIOS.put("Arroz", BigDecimal.valueOf(120));
        PRECIOS.put("Café Premium", BigDecimal.valueOf(300));
        PRECIOS.put("Café", BigDecimal.valueOf(250));
        PRECIOS.put("Maíz", BigDecimal.valueOf(180));
        PRECIOS.put("Frijol", BigDecimal.valueOf(220));
    }

    public FacturaDTO procesarNuevaCosecha(EventoCosechaDTO evento) {
        log.info("Procesando nueva cosecha para facturación: {}", evento.getPayload().getCosechaId());

        try {
            // Verificar si ya existe una factura para esta cosecha
            Optional<Factura> facturaExistente = facturaRepository.findByCosechaId(evento.getPayload().getCosechaId());
            if (facturaExistente.isPresent()) {
                log.warn("Ya existe una factura para la cosecha: {}", evento.getPayload().getCosechaId());
                return convertirADTO(facturaExistente.get());
            }

            // Calcular el monto de la factura
            BigDecimal monto = calcularMonto(evento.getPayload().getProducto(), evento.getPayload().getToneladas());

            // Crear la factura
            Factura factura = Factura.builder()
                    .cosechaId(evento.getPayload().getCosechaId())
                    .producto(evento.getPayload().getProducto())
                    .toneladas(evento.getPayload().getToneladas())
                    .monto(monto)
                    .pagado(false)
                    .build();

            factura = facturaRepository.save(factura);
            log.info("Factura creada con ID: {} para cosecha: {}", factura.getFacturaId(), factura.getCosechaId());

            // Notificar al microservicio central
            notificarEstadoFacturada(factura.getCosechaId(), factura.getFacturaId());

            return convertirADTO(factura);

        } catch (Exception e) {
            log.error("Error al procesar nueva cosecha: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar factura", e);
        }
    }

    private BigDecimal calcularMonto(String producto, BigDecimal toneladas) {
        BigDecimal precioBase = PRECIOS.getOrDefault(producto, BigDecimal.valueOf(100)); // Precio por defecto
        BigDecimal monto = toneladas.multiply(precioBase);

        log.info("Calculando monto: {} toneladas x ${} = ${}", toneladas, precioBase, monto);
        return monto;
    }

    private void notificarEstadoFacturada(String cosechaId, String facturaId) {
        try {
            String url = centralServiceUrl + "/api/cosechas/" + cosechaId + "/estado";

            ActualizarEstadoCosechaDTO actualizacion = ActualizarEstadoCosechaDTO.builder()
                    .estado("FACTURADA")
                    .facturaId(facturaId)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ActualizarEstadoCosechaDTO> request = new HttpEntity<>(actualizacion, headers);

            restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            log.info("Estado actualizado en microservicio central para cosecha: {}", cosechaId);

        } catch (Exception e) {
            log.error("Error al notificar al microservicio central: {}", e.getMessage(), e);
            // No lanzamos excepción para no fallar el proceso de facturación
        }
    }

    public List<FacturaDTO> obtenerTodasLasFacturas() {
        return facturaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Optional<FacturaDTO> obtenerFacturaPorId(String facturaId) {
        return facturaRepository.findById(facturaId)
                .map(this::convertirADTO);
    }

    public Optional<FacturaDTO> obtenerFacturaPorCosechaId(String cosechaId) {
        return facturaRepository.findByCosechaId(cosechaId)
                .map(this::convertirADTO);
    }

    public List<FacturaDTO> obtenerFacturasPendientes() {
        return facturaRepository.findByPagado(false).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public FacturaDTO marcarComoPagada(String facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + facturaId));

        factura.setPagado(true);
        factura = facturaRepository.save(factura);

        log.info("Factura {} marcada como pagada", facturaId);
        return convertirADTO(factura);
    }

    public Map<String, BigDecimal> obtenerPrecios() {
        return new HashMap<>(PRECIOS);
    }

    private FacturaDTO convertirADTO(Factura factura) {
        return FacturaDTO.builder()
                .facturaId(factura.getFacturaId())
                .cosechaId(factura.getCosechaId())
                .monto(factura.getMonto())
                .pagado(factura.getPagado())
                .fechaCreacion(factura.getFechaCreacion())
                .producto(factura.getProducto())
                .toneladas(factura.getToneladas())
                .build();
    }
}
