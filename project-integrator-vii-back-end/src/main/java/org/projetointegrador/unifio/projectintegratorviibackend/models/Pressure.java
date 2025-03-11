package org.projetointegrador.unifio.projectintegratorviibackend.models;

import jakarta.persistence.*;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.MeasurementContext;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pressure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer systolic;
    private Integer diastolic;
    private Integer heartbeat;
    private LocalDateTime measurementTime;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

}
