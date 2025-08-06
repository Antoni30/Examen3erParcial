package ec.edu.espe.inventario.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private String evento;
    private String cosecha_id;
    private String producto;
    private double tonelada;
    private int status;
}
