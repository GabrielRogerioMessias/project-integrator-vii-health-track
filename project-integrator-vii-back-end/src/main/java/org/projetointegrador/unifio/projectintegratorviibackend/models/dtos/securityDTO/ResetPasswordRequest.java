package org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}
