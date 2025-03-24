package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientRegistrationDTO {
    @Email(message = "please enter a valid email format")
    private String email;
    @NotBlank(message = "email of user may not blank")
    private String password;
    @NotBlank(message = "name of user may not blank")
    private String name;
    @NotNull(message = "birth of patient may not null")
    private Date birth;
    @NotBlank(message = "phone of patient may not blank")
    private String phone;
    @NotNull(message = "weight of patient may not blank")
    private Double weight;
}
