package org.projetointegrador.unifio.projectintegratorviibackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Systolic of Pressure may not blank")
    private Integer systolic;
    @NotNull(message = "Diastolic of Pressure may not blank")
    private Integer diastolic;
    @NotNull(message = "HeartBeat of Pressure may not blank")
    private Integer heartbeat;
    @NotNull(message = "Measurement time of Pressure may not blank")
    private LocalDateTime measurementTime;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

}
