package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions.StandardError;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientUpdateDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/patients")
@Tag(name = "Paciente", description = "Controlador para registrar, e atualizar dados do usuário.")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Salva dados do paciente",
            description = "Registra um novo paciente no sistema a partir dos dados informados",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Paciente registrado com sucesso.", content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao registrar o paciente", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
            }
    )
    @PostMapping(value = "/register-patient")
    public ResponseEntity<PatientResponseDTO> registerPatient(@RequestBody PatientRegistrationDTO registerDTO) {
        PatientResponseDTO patient = patientService.registerPatient(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @Operation(
            summary = "Atualizar dados do paciente",
            description = "Atualiza os dados do paciente autenticado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do paciente atualizado com sucesso", content = @Content(schema = @Schema(implementation = PatientUpdateDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar os dados do paceinte", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PutMapping
    public ResponseEntity<PatientResponseDTO> updatePatient(@RequestBody PatientUpdateDTO patientUpdateDTO) {
        return ResponseEntity.ok().body(this.patientService.updatePatient(patientUpdateDTO));
    }

}
