package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Data
public class PatientUpdateDTO {
    private String name;
    @Past(message = "date outside the allowed range")
    private Date birth;
    @CPF(message = "please enter with a valid CPF")
    private String CPF;
    private String phone;
    @DecimalMin(value = "3.40", message = "please, insert a valid weight")
    @DecimalMax(value = "350.00", message = "please, insert a valid weight")
    private Double weight;
    @DecimalMin(value = "0.40", message = "please, insert a valid height")
    @DecimalMax(value = "2.70", message = "please, insert a valid height")
    private Double height;
}
