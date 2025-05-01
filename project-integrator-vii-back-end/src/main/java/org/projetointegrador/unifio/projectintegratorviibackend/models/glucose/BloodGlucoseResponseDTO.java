package org.projetointegrador.unifio.projectintegratorviibackend.models.glucose;

import lombok.*;
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
