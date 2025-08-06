package ec.edu.espe.cosecha_agricultor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cosechas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cosecha {

    @Id
    @GeneratedValue
    @Column(name = "cosecha_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agricultor_id", nullable = false)
    private Agricultor agricultor;

    @Column(name = "producto", nullable = false, length = 100)
    private String producto;

    @Column(name = "toneladas", nullable = false)
    private Double toneladas;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // NUEVOS CAMPOS: Para el manejo de estados y facturación
    @Column(name = "estado", length = 50)
    @Builder.Default
    private String estado = "REGISTRADA";

    @Column(name = "factura_id", length = 100)
    private String facturaId;

}
