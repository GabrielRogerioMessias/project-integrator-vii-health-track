package org.projetointegrador.unifio.projectintegratorviibackend.models.pressure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureResponseDTO {
    private Long id;
    private Integer systolic;
    private Integer diastolic;
    private Integer heartbeat;
    private LocalDateTime measurementTime;
}
