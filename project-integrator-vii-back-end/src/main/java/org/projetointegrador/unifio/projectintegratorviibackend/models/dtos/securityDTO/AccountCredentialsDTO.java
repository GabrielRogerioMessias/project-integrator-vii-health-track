package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AccountCredentialsDTO {
    private String username;
    private String password;
}