package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> registerPatient(@RequestBody PatientRegistrationDTO registerDTO) {
        PatientResponseDTO patient = patientService.registerPatient(registerDTO);
        return ResponseEntity.ok(patient);
    }
}
