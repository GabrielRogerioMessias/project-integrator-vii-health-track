package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Schema(description = "Token usado para a criação de uma nova senha", example = "aYzHHa...")
    @NotBlank(message = "The token field cannot be empty")
    private String token;
    @Schema(description = "Nova senha para o usuário", example = "Exemplo321")
    @NotBlank(message = "The password field cannot be empty")
    private String newPassword;
}
