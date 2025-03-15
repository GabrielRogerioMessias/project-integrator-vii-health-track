package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientRegistrationDTO {
    private String email;
    private String password;
    private String name;
    private Date birth;
    private String phone;
    private Double weight;
}
