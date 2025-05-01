package org.projetointegrador.unifio.projectintegratorviibackend.models.pressure;

import jakarta.persistence.*;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;

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
