package org.projetointegrador.unifio.projectintegratorviibackend.models.glucose;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Data é hora da medição", example = "2025-05-01T10:30:22.123Z")
    @NotNull(message = "Measurement Time of BloodGlucose may not blank")
    private LocalDateTime measurementTime;

    @Schema(description = "Valor da glicose no ato da medição", example = "99")
    @NotNull(message = "Glucose value of BloodGlucose may not blank")
    private Integer glucoseValue;

    @Schema(description = "Deve conter a sigla da enum", example = "J -> Jejum , PR-> antes da refeição , DR -> durante a refeição, AP -> Após a refeição ")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Measurement context of BloodGlucose may not blank")
    private MeasurementContext context;
}
