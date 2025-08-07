package ec.edu.espe.inventario.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private String evento;
    private String cosecha_id;    // Cambiado de UUID a String para coincidir con las entidades
    private String producto;
    private double tonelada;
    private int status;           // Cambiado de double a int para coincidir con la entidad Evento
}
