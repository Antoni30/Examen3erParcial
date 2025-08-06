package ec.edu.espe.cosecha_agricultor.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CosechaDTO {
    private UUID agricultorId;
    private String producto;
    private Double toneladas;
    private String ubicacion;
}
