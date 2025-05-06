package org.projetointegrador.unifio.projectintegratorviibackend.models.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TokenDTO {
    @Schema(description = "Email do usuário que efetuou o login", example = "exemplo@exemplo.com")
    private String username;
    @Schema(description = "Boolean se está autenticado ou não", example = "true")
    private Boolean authenticated;
    @Schema(description = "Data de criação", example = "2025-05-06T22:48:17.784+00:00")
    private Date created;
    @Schema(description = "Data de validade", example = "2025-05-06T23:48:17.784+00:00")
    private Date expiration;
    @Schema(description = "Contém o token de acesso", example = "ayHaJZBAB...")
    private String accessToken;
    @Schema(description = "Contém o refreshToekn", example = "AyNgVBZA...")
    private String refreshToken;
}
