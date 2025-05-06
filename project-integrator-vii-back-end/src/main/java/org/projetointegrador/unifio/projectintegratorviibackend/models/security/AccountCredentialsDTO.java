package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsDTO {
    @Schema(description = "Email para efetuar o login", example = "exemplo@exemplo.com")
    private String email;
    @Schema(description = "Senha para efetuar o login", example = "exemplo123")
    private String password;
}