package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.BloodGlucoseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/glucose")
public class BloodGlucoseController {
    private final BloodGlucoseService bloodGlucoseService;

    public BloodGlucoseController(BloodGlucoseService bloodGlucoseService) {
        this.bloodGlucoseService = bloodGlucoseService;
    }

    @PostMapping
    public ResponseEntity<BloodGlucoseResponseDTO> registerGlucose(@RequestBody BloodGlucoseRegistrationDTO bloodGlucose) {
        BloodGlucoseResponseDTO newBloodGlucose = bloodGlucoseService.registerGlucose(bloodGlucose);
        return ResponseEntity.ok().body(newBloodGlucose);
    }
}
