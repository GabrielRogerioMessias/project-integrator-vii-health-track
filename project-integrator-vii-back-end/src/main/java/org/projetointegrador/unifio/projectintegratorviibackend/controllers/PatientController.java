package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientUpdateDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping(value = "/register-patient")
    public ResponseEntity<PatientResponseDTO> registerPatient(@RequestBody PatientRegistrationDTO registerDTO) {
        PatientResponseDTO patient = patientService.registerPatient(registerDTO);
        return ResponseEntity.ok(patient);
    }

    @PutMapping
    public ResponseEntity<PatientResponseDTO> updatePatient(@RequestBody PatientUpdateDTO patientUpdateDTO) {
        return ResponseEntity.ok().body(this.patientService.updatePatient(patientUpdateDTO));
    }

}
