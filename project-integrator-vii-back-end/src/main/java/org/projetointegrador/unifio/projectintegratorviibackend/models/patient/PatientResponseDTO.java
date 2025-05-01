package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String name;
    private String CPF;
    private Date birth;
    private String phone;
    private Double weight;
    private Double height;
    private Date createdAt;
}
