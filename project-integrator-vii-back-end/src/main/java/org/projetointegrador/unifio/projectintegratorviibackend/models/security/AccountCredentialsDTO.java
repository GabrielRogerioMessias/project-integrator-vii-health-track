package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsDTO {
    private String email;
    private String password;
}