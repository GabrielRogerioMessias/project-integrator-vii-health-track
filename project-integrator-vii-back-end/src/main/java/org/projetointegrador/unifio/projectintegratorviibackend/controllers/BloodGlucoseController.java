package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.BloodGlucoseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        bloodGlucoseService.deleteGlucose(idGlucose);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BloodGlucoseResponseDTO>> findGlucoseByCurrentUser() {
        return ResponseEntity.ok().body(bloodGlucoseService.findAllGlucoseByCurrentUser());
    }

    @GetMapping(value = "/{idGlucose}")
    public ResponseEntity<BloodGlucoseResponseDTO> findGlucoseById(@PathVariable Long idGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.findGlucoseById(idGlucose));
    }

    @PostMapping
    public ResponseEntity<BloodGlucoseResponseDTO> registerGlucose(@RequestBody BloodGlucoseRegistrationDTO bloodGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.registerGlucose(bloodGlucose));
    }

    @PutMapping(value = "/{idGlucose}")
    public ResponseEntity<BloodGlucoseResponseDTO> updateGlucose(@RequestBody BloodGlucoseRegistrationDTO glucoseUpdated, @PathVariable Long idGlucose) {
        return ResponseEntity.ok().body(bloodGlucoseService.updateGlucose(glucoseUpdated, idGlucose));
    }

    @GetMapping(value = "/find-by-date")
    public ResponseEntity<List<BloodGlucoseResponseDTO>> getGlucoseByDate(
            @RequestParam LocalDateTime initialDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok().body(bloodGlucoseService.getGlucoseByDate(initialDate, endDate));
    }
}
