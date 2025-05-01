package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRegistrationDTO {
    @Email(message = "please enter a valid email format")
    private String email;
    @NotBlank(message = "email of user may not blank")
    private String password;
    @NotBlank(message = "name of user may not blank")
    @Size(min = 3, max = 50, message = "please, enter with a valid name")
    private String name;
    @NotBlank(message = "CPF of patient may not blank")
    @CPF(message = "please enter with a valid CPF")
    private String CPF;
    @NotNull(message = "birth of patient may not null")
    @Past(message = "date outside the allowed range")
    private Date birth;
    @NotBlank(message = "phone of patient may not blank")
    private String phone;
    @NotNull(message = "weight of patient may not blank")
    @DecimalMin(value = "3.40", message = "please, insert a valid weight")
    @DecimalMax(value = "350.00", message = "please, insert a valid weight")
    private Double weight;
    @DecimalMin(value = "0.40", message = "please, insert a valid height")
    @DecimalMax(value = "2.70", message = "please, insert a valid height")
    @NotNull(message = "height of patient may not blank")
    private Double height;
}
