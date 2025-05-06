package org.projetointegrador.unifio.projectintegratorviibackend.models.pressure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureResponseDTO {
    @Schema(description = "id da pressão", example = "1")
    private Long id;
    @Schema(description = "Valor da pressão sistólica", example = "110")
    private Integer systolic;
    @Schema(description = "Valor da pressão diastólica", example = "90")
    private Integer diastolic;
    @Schema(description = "Batimentos cardiácos do paciente", example = "110")
    private Integer heartbeat;
    @Schema(description = "Data é hora da medição", example = "2025-05-01T10:30:22.123Z")
    private LocalDateTime measurementTime;
}
