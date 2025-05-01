package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import lombok.Data;

import java.util.Date;

@Data
public class PatientUpdateDTO {
    private String name;
    private Date birth;
    private String CPF;
    private String phone;
    private Double weight;
}
