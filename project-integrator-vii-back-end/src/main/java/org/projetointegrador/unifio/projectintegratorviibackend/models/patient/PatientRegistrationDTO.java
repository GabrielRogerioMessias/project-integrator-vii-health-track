package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para o cadastro de novos pacientes")
public class PatientRegistrationDTO {
    @NotBlank(message = "name of user may not blank")
    @Size(min = 3, max = 50, message = "please, enter with a valid name")
    @Schema(description = "Nome do paciente, deve conter no nonimo 3 caracteres, e no máximo 50", example = "Exemplo de Exemplo")
    private String name;
    @Email(message = "please enter a valid email format")
    @Schema(description = "Endereço de e-mail do paciente, usado para credencial do login", example = "exemplo@exemplo.com")
    private String email;
    @NotBlank(message = "email of user may not blank")
    @Schema(description = "Senha do paciente, usada para credencial do login", example = "exemplo123")
    private String password;
    @NotBlank(message = "CPF of patient may not blank")
    @CPF(message = "please enter with a valid CPF")
    @Schema(description = "Deve conter o CPF do paciente sem pontos e sem espaço, o CPF deve ser válido", example = "12345678912")
    private String CPF;
    @NotNull(message = "birth of patient may not null")
    @Past(message = "date outside the allowed range")
    @Schema(description = "Data de nascimento do paciente, não aceita datas no futuro", example = "2003-01-01")
    private Date birth;
    @NotBlank(message = "phone of patient may not blank")
    @Schema(description = "Telefone do paciente", example = "04399696-9696")
    private String phone;
    @NotNull(message = "weight of patient may not blank")
    @DecimalMin(value = "3.40", message = "please, insert a valid weight")
    @DecimalMax(value = "350.00", message = "please, insert a valid weight")
    @Schema(description = "Peso do paciente, deve estar entre 3.4 a 350.0, não aceita valores fora desse intervalo", example = "70.00")
    private Double weight;
    @DecimalMin(value = "0.40", message = "please, insert a valid height")
    @DecimalMax(value = "2.70", message = "please, insert a valid height")
    @NotNull(message = "height of patient may not blank")
    @Schema(description = "Altura do paciente, deve estar entre 0.40 metros a 2.70 metros, não aceita valores fora desse intervalo", example = "1.70")
    private Double height;
}
