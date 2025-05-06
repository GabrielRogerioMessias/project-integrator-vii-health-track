package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordDTO {
    @Schema(description = "E-mail do usuário que esqueceu a senha", example = "exemplo@exemplo.com")
    @NotBlank
    @Email(message = "please enter a valid email format")
    private String email;
}
