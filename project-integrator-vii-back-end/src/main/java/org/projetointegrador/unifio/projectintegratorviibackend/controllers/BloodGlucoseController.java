package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.BloodGlucoseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/glucose")
public class BloodGlucoseController {
    private final BloodGlucoseService bloodGlucoseService;

    public BloodGlucoseController(BloodGlucoseService bloodGlucoseService) {
        this.bloodGlucoseService = bloodGlucoseService;
    }

    @DeleteMapping(value = "/{idGlucose}")
    public ResponseEntity<Void> deleteGlucose(@PathVariable Long idGlucose) {
        bloodGlucoseService.deleteGlucoseRegister(idGlucose);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<BloodGlucoseResponseDTO>> findGlucoseByCurrentUser() {
        return ResponseEntity.ok().body(bloodGlucoseService.findAllGlucoseByCurrentUser());
    }

    @PostMapping
    public ResponseEntity<BloodGlucoseResponseDTO> registerGlucose(@RequestBody BloodGlucoseRegistrationDTO bloodGlucose) {
        BloodGlucoseResponseDTO newBloodGlucose = bloodGlucoseService.registerGlucose(bloodGlucose);
        return ResponseEntity.ok().body(newBloodGlucose);
    }
}
