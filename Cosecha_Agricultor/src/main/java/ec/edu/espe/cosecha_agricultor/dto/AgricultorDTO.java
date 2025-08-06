package ec.edu.espe.cosecha_agricultor.dto;

import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgricultorDTO {
    private UUID id;
    private String nombre;
}
