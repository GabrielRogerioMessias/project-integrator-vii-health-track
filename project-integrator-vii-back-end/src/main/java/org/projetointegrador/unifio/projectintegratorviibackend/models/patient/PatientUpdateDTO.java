package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Data
@Schema(description = "DTO utilizado para atualizar dados do paciente logado, caso o campo seja nulo ou esteja em branco, o sistema mantem o dado já cadastrado anteriormente.")
public class PatientUpdateDTO {
    @Schema(description = "Nome do paciente, deve conter no nonimo 3 caracteres, e no máximo 50", example = "Exemplo de Exemplo")
    private String name;
    @Past(message = "date outside the allowed range")
    @Schema(description = "Data de nascimento do paciente, não aceita datas no futuro", example = "2003-01-01")
    private Date birth;
    @CPF(message = "please enter with a valid CPF")
    @Schema(description = "Deve conter o CPF do paciente sem pontos e sem espaço, o CPF deve ser válido", example = "12345678912")
    private String CPF;
    private String phone;
    @DecimalMin(value = "3.40", message = "please, insert a valid weight")
    @DecimalMax(value = "350.00", message = "please, insert a valid weight")
    @Schema(description = "Peso do paciente, deve estar entre 3.4 a 350.0, não aceita valores fora desse intervalo", example = "70.00")
    private Double weight;
    @DecimalMin(value = "0.40", message = "please, insert a valid height")
    @DecimalMax(value = "2.70", message = "please, insert a valid height")
    @Schema(description = "Altura do paciente, deve estar entre 0.40 metros a 2.70 metros, não aceita valores fora desse intervalo", example = "1.70")
    private Double height;
}
