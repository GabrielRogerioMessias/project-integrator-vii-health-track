package org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Modelo padrão de resposta para exceções e erros da API")
public class StandardError {
    @Schema(description = "Momento em que o erro ocorreu (em formato UTC ISO 8601)", example = "2025-05-01T14:33:22.123Z")
    private Instant timestamp;
    @Schema(description = "Código HTTP correspondente ao erro", example = "404")
    private Integer status;
    @Schema(description = "Tipo genérico de erro ocorrido", example = "Recurso não encontrado")
    private String error;
    @Schema(description = "Mensagem detalhada da exceção para auxiliar no debug", example = "Paciente com ID 10 não encontrado")
    private String message;
    @Schema(description = "Caminho (URL) da requisição que gerou o erro", example = "/api/patients/10")
    private String path;
}
