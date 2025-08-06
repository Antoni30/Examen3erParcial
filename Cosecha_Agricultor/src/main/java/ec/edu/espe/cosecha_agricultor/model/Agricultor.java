package ec.edu.espe.cosecha_agricultor.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agricultores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agricultor {

    @Id
    @GeneratedValue
    @Column(name = "agricultor_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

}
