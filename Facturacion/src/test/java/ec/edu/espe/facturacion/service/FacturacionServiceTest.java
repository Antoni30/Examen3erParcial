package ec.edu.espe.facturacion.service;

import ec.edu.espe.facturacion.dto.EventoCosechaDTO;
import ec.edu.espe.facturacion.dto.FacturaDTO;
import ec.edu.espe.facturacion.model.Factura;
import ec.edu.espe.facturacion.repository.FacturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturacionServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FacturacionService facturacionService;

    private EventoCosechaDTO eventoCosecha;
    private Factura facturaMock;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        EventoCosechaDTO.PayloadDTO payload = EventoCosechaDTO.PayloadDTO.builder()
                .cosechaId("test-cosecha-id")
                .producto("Arroz Oro")
                .toneladas(BigDecimal.valueOf(12.5))
                .build();

        eventoCosecha = EventoCosechaDTO.builder()
                .eventId("test-event-id")
                .eventType("nueva_cosecha")
                .timestamp(LocalDateTime.now())
                .payload(payload)
                .build();

        facturaMock = Factura.builder()
                .facturaId("test-factura-id")
                .cosechaId("test-cosecha-id")
                .producto("Arroz Oro")
                .toneladas(BigDecimal.valueOf(12.5))
                .monto(BigDecimal.valueOf(1500.00))
                .pagado(false)
                .build();
    }

    @Test
    void testProcesarNuevaCosecha_Success() {
        // Arrange
        when(facturaRepository.findByCosechaId("test-cosecha-id")).thenReturn(Optional.empty());
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaMock);

        // Act
        FacturaDTO resultado = facturacionService.procesarNuevaCosecha(eventoCosecha);

        // Assert
        assertNotNull(resultado);
        assertEquals("test-cosecha-id", resultado.getCosechaId());
        assertEquals("Arroz Oro", resultado.getProducto());
        assertEquals(BigDecimal.valueOf(12.5), resultado.getToneladas());
        assertEquals(BigDecimal.valueOf(1500.00), resultado.getMonto());
        assertFalse(resultado.getPagado());

        verify(facturaRepository).findByCosechaId("test-cosecha-id");
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void testProcesarNuevaCosecha_FacturaYaExiste() {
        // Arrange
        when(facturaRepository.findByCosechaId("test-cosecha-id")).thenReturn(Optional.of(facturaMock));

        // Act
        FacturaDTO resultado = facturacionService.procesarNuevaCosecha(eventoCosecha);

        // Assert
        assertNotNull(resultado);
        assertEquals("test-cosecha-id", resultado.getCosechaId());

        verify(facturaRepository).findByCosechaId("test-cosecha-id");
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void testMarcarComoPagada_Success() {
        // Arrange
        when(facturaRepository.findById("test-factura-id")).thenReturn(Optional.of(facturaMock));
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaMock);

        // Act
        FacturaDTO resultado = facturacionService.marcarComoPagada("test-factura-id");

        // Assert
        assertNotNull(resultado);
        verify(facturaRepository).findById("test-factura-id");
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void testMarcarComoPagada_FacturaNoEncontrada() {
        // Arrange
        when(facturaRepository.findById("test-factura-id")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            facturacionService.marcarComoPagada("test-factura-id");
        });

        verify(facturaRepository).findById("test-factura-id");
        verify(facturaRepository, never()).save(any(Factura.class));
    }
}
