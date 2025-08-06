package ec.edu.espe.facturacion.dto;

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
    private String producto;
    private double tonelada;
    private double status;
}
