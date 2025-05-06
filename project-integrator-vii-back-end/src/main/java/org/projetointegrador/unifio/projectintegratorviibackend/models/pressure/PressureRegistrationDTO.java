package org.projetointegrador.unifio.projectintegratorviibackend.models.pressure;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureRegistrationDTO {
    @NotNull(message = "Systolic of Pressure may not blank")
    @Schema(description = "Valor da pressão sistólica", example = "110")
    private Integer systolic;
    @NotNull(message = "Diastolic of Pressure may not blank")
    @Schema(description = "Valor da pressão diastólica", example = "90")
    private Integer diastolic;
    @Schema(description = "Batimentos cardiácos do paciente", example = "110")
    private Integer heartbeat;
    @NotNull(message = "Measurement time of Pressure may not blank")
    @Schema(description = "Data é hora da medição", example = "2025-05-01T10:30:22.123Z")
    private LocalDateTime measurementTime;
}
