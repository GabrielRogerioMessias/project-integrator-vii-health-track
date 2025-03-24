package org.projetointegrador.unifio.projectintegratorviibackend.models;

import jakarta.persistence.*;
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
public class BloodGlucose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Glucose value of BloodGlucose may not blank")
    private Integer glucoseValue;
    @NotNull(message = "Measurement Time of BloodGlucose may not blank")
    private LocalDateTime measurementTime;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Measurement context of BloodGlucose may not blank")
    private MeasurementContext context;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


}
