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
public class BloodGlucose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer glucoseValue;
    private LocalDateTime measurementTime;
    @Enumerated(EnumType.STRING)
    private MeasurementContext context;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


}
