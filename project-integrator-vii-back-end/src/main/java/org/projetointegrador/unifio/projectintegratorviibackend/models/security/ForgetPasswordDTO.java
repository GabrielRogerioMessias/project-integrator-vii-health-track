package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordDTO {
    @NotBlank
    @Email(message = "please enter a valid email format")
    private String email;
}
