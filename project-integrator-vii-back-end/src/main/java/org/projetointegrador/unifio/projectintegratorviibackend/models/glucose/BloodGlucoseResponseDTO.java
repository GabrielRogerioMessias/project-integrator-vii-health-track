package org.projetointegrador.unifio.projectintegratorviibackend.models.glucose;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.MeasurementContext;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodGlucoseResponseDTO {
    @Schema(description = "Id da glicose", example = "1")
    private Long id;
    @Schema(description = "Valor da glicose", example = "99")
    private Integer glucoseValue;
    @Schema(description = "Data é hora da medição", example = "2025-05-01T10:30:22.123Z")
    private LocalDateTime measurementTime;
    @Schema(description = "Deve conter a sigla da enum", example = "J -> Jejum , PR-> antes da refeição , DR -> durante a refeição, AP -> Após a refeição ")
    private MeasurementContext context;
}