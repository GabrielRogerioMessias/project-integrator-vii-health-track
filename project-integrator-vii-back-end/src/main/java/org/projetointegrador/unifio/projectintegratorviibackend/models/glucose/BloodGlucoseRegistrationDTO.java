package org.projetointegrador.unifio.projectintegratorviibackend.models.glucose;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.MeasurementContext;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodGlucoseRegistrationDTO {
    @NotNull(message = "Glucose value of BloodGlucose may not blank")
    private Integer glucoseValue;
    @NotNull(message = "Measurement Time of BloodGlucose may not blank")
    private LocalDateTime measurementTime;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Measurement context of BloodGlucose may not blank")
    private MeasurementContext context;
}
