package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions.StandardError;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.BloodGlucoseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/glucose")
@Tag(name = "Glicose", description = "Controlador para registrar, editar, excluir e filtrar registros de glicose.")
public class BloodGlucoseController {
    private final BloodGlucoseService bloodGlucoseService;

    public BloodGlucoseController(BloodGlucoseService bloodGlucoseService) {
        this.bloodGlucoseService = bloodGlucoseService;
    }

    @Operation(
            summary = "Exclui o registro de glicose",
            description = "Exclui o registro de glicose, através do id presente no parâmetro.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "O registro de glicose foi excluido com sucesso.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "O registro de glicose não foi encontrado com o id passado", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    @DeleteMapping(value = "/{idGlucose}")
    public ResponseEntity<Void> deleteGlucose(@PathVariable Long idGlucose) {
        bloodGlucoseService.deleteGlucose(idGlucose);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Retorna todos os registros de glicose",
            description = "Retorna todos os registros de glicose do paciente logado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de glicoses com sucesso", content = @Content(schema = @Schema(implementation = BloodGlucoseResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<BloodGlucoseResponseDTO>> findGlucoseByCurrentUser() {
        return ResponseEntity.ok().body(bloodGlucoseService.findAllGlucoseByCurrentUser());
    }

    @Operation(
            summary = "Retorna a glicose pelo id",
            description = "Retorna um DTO da glicose contendo o id passado no parâmetro",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna a glicose com o id passado por parâmetro com sucesso", content = @Content(schema = @Schema(implementation = BloodGlucoseResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Glicose não encontrada com o id passado", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @GetMapping(value = "/{idGlucose}")
    public ResponseEntity<BloodGlucoseResponseDTO> findGlucoseById(@Schema(description = "Deve conter o id da glicose desejada", example = "1") @PathVariable Long idGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.findGlucoseById(idGlucose));
    }

    @Operation(
            summary = "Registra uma glicose",
            description = "Registra uma nova glicose com os dados informados pelo paciente",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Glicose registrada com sucesso", content = @Content(schema = @Schema(implementation = BloodGlucoseResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao registrar glicose", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<BloodGlucoseResponseDTO> registerGlucose(@RequestBody BloodGlucoseRegistrationDTO bloodGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.registerGlucose(bloodGlucose));
    }

    @Operation(
            summary = "Atualiza os dados da glicose",
            description = "Atualiza os dados da glicose com o id informado, e com os novos dados enviados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados da glicose atualizados com sucesso", content = @Content(schema = @Schema(implementation = BloodGlucoseResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Glicose não encontrada com o ID do parâmetro", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar os dados da glicose", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PutMapping(value = "/{idGlucose}")
    public ResponseEntity<BloodGlucoseResponseDTO> updateGlucose(@RequestBody BloodGlucoseRegistrationDTO glucoseUpdated, @Schema(description = "Deve conter o id da glicose que será atualizada", example = "1") @PathVariable Long idGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.updateGlucose(glucoseUpdated, idGlucose));
    }

    @Operation(
            summary = "Buscar registros de glicose por data",
            description = "Permite filtrar registros de glicose entre duas datas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista contendo as glicoses registradas entre os periodos parametrizados", content = @Content(schema = @Schema(implementation = BloodGlucoseResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Não foi possível filtrar por data / parametros nulos", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @GetMapping(value = "/find-by-date")
    public ResponseEntity<List<BloodGlucoseResponseDTO>> getGlucoseByDate(
            @Schema(description = "Deve conter a data inicial do filtro", example = "2025-05-01T08:30:00.123Z")
            @RequestParam(required = false) LocalDateTime initialDate,
            @Schema(description = "Deve conter a data final do filtro", example = "2025-05-01T18:00:00.123Z")
            @RequestParam(required = false) LocalDateTime endDate) {
        return ResponseEntity.ok().body(bloodGlucoseService.getGlucoseByDate(initialDate, endDate));
    }
}
