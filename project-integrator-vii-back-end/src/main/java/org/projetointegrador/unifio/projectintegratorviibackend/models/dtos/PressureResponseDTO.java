package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.projetointegrador.unifio.projectintegratorviibackend.models.Pressure;

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

    public PressureResponseDTO(Pressure pressure) {
        this.id = pressure.getId();
        this.systolic = pressure.getSystolic();
        this.diastolic = pressure.getDiastolic();
        this.heartbeat = pressure.getHeartbeat();
        this.measurementTime = pressure.getMeasurementTime();
    }
}
