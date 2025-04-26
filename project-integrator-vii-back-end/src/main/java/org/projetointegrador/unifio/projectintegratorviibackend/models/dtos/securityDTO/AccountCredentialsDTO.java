package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsDTO {
    private String email;
    private String password;
}