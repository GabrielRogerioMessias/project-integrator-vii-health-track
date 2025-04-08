package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureRegistrationDTO {
    @NotNull(message = "Systolic of Pressure may not blank")
    private Integer systolic;
    @NotNull(message = "Diastolic of Pressure may not blank")
    private Integer diastolic;
    @NotNull(message = "HeartBeat of Pressure may not blank")
    private Integer heartbeat;
    @NotNull(message = "Measurement time of Pressure may not blank")
    private LocalDateTime measurementTime;
}
