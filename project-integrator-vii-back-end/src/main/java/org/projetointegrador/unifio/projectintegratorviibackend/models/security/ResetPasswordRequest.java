package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "The token field cannot be empty")
    private String token;
    @NotBlank(message = "The password field cannot be empty")
    private String newPassword;
}
