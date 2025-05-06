package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions.StandardError;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.PressureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/pressure")
@Tag(name = "Pressão", description = "Controlador para registrar, editar, excluir e filtrar registros de pressão arterial.")
public class PressureController {
    private final PressureService pressureService;

    public PressureController(PressureService pressureService) {
        this.pressureService = pressureService;
    }

    @Operation(
            summary = "Exclui o registro de pressão",
            description = "Exclui o registro de pressão, através do id presente no parâmetro.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "o registro de pressão foi excluido com sucesso.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "o registro de pressão não foi encontrado com o ID passado", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    @DeleteMapping(value = "/{idPressure}")
    public ResponseEntity<Void> deletePressure(@PathVariable @Schema(description = "Deve conter o id da pressão excluída", example = "1") Long idPressure) {
        pressureService.deletePressure(idPressure);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Retorna todas os registros de pressão",
            description = "Retorna todos os registros de pressão do paciente logado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "retorna a lista de pressões com sucesso", content = @Content(schema = @Schema(implementation = PressureResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    public ResponseEntity<List<PressureResponseDTO>> findAllPressureByCurrentUser() {
        return ResponseEntity.ok().body(pressureService.findAllPressureByCurrentUser());
    }

    @Operation(
            summary = "Retorna a pressão pelo id",
            description = "Retorna um DTO de pressão contendo o ID passado no parâmetro",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna a pressão com o id passado por parâmetro com sucesso", content = @Content(schema = @Schema(implementation = PressureResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pressão não encontrada com o id passado", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @GetMapping(value = "/{idPressure}")
    public ResponseEntity<PressureResponseDTO> findPressureById(@PathVariable @Schema(description = "Deve conter o id da pressão desejada", example = "1") Long idPressure) {
        return ResponseEntity.ok().body(pressureService.findPressureById(idPressure));
    }

    @Operation(
            summary = "Registra uma pressão",
            description = "Registra uma nova pressão com os dados informados pelo paciente",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pressão registrada com sucesso", content = @Content(schema = @Schema(implementation = PressureResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao registrar pressão", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<PressureResponseDTO> registerPressure(@RequestBody PressureRegistrationDTO pressure) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pressureService.registerPressure(pressure));
    }

    @Operation(
            summary = "Atualiza os dados da pressão",
            description = "Atualiza os dados da pressão com o id informado, e com os novos dados enviados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados de pressão atualizados com sucesso", content = @Content(schema = @Schema(implementation = PressureResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pressão não encontrada com o ID do parâmetro", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar os dados da pressão", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PutMapping(value = "/{idPressure}")
    public ResponseEntity<PressureResponseDTO> updatePressure(@RequestBody PressureRegistrationDTO updatedPressure, @PathVariable @Schema(description = "Deve conter o id da pressão a ser atualizada", example = "1") Long idPressure) {
        return ResponseEntity.ok().body(pressureService.updatePressure(updatedPressure, idPressure));
    }

    @Operation(
            summary = "Buscar registros de pressão por data",
            description = "Permite filtrar registros de pressão entre duas datas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista contendo as pressões registradas entre os periodos parametrizados", content = @Content(schema = @Schema(implementation = PressureResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Não foi possível filtrar por data / parametros nulos", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @GetMapping(value = "/find-by-date")
    public ResponseEntity<List<PressureResponseDTO>> getPressureByDate(
            @Schema(description = "Deve conter a data inicial do filtro", example = "2025-05-01T08:30:00.123Z")
            @RequestParam(required = false) LocalDateTime initialDate,
            @Schema(description = "Deve conter a data final do filtro", example = "2025-05-01T18:00:00.123Z")
            @RequestParam(required = false) LocalDateTime endDate) {
        return ResponseEntity.ok().body(pressureService.getPressureByDate(initialDate, endDate));
    }

}
