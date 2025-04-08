package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.MeasurementContext;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodGlucoseResponseDTO {
    private Long id;
    private Integer glucoseValue;
    private LocalDateTime measurementTime;
    private MeasurementContext context;

    public BloodGlucoseResponseDTO(BloodGlucose bloodGlucose) {
        this.id = bloodGlucose.getId();
        this.glucoseValue = bloodGlucose.getGlucoseValue();
        this.measurementTime = bloodGlucose.getMeasurementTime();
        this.context = bloodGlucose.getContext();
    }
}
