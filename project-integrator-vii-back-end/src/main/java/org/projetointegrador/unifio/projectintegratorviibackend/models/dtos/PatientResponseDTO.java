package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String name;
    private Date birth;
    private String phone;
    private Double weight;
    private Date createdAt;

    public PatientResponseDTO(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.birth = patient.getBirth();
        this.phone = patient.getPhone();
        this.weight = patient.getWeight();
        this.createdAt = patient.getCreatedAt();
    }
}
