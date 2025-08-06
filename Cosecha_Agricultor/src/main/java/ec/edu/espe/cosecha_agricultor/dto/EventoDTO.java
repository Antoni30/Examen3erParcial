package ec.edu.espe.cosecha_agricultor.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private String evento;
    private UUID cosecha_id;
    private double tonelada;
    private double status;
}