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
}
