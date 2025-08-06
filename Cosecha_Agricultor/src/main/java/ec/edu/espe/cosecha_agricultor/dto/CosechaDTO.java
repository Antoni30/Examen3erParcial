package ec.edu.espe.cosecha_agricultor.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosechaDTO {

    private UUID id;

    @NotNull(message = "El ID del agricultor es obligatorio")
    private UUID agricultorId;

    @NotBlank(message = "El producto es obligatorio")
    @Size(max = 100, message = "El producto debe tener máximo 100 caracteres")
    private String producto;

    @NotNull(message = "Las toneladas son obligatorias")
    @Positive(message = "Las toneladas deben ser positivas")
    private Double toneladas;

    private String ubicacion;

    private LocalDateTime fechaRegistro;
}
