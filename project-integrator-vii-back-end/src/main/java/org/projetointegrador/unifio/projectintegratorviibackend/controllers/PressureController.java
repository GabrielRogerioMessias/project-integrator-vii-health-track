package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PressureRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.PressureResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.services.PressureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/pressure")
public class PressureController {
    private final PressureService pressureService;

    public PressureController(PressureService pressureService) {
        this.pressureService = pressureService;
    }

    @DeleteMapping(value = "/{idPressure}")
    public ResponseEntity<Void> deletePressure(@PathVariable Long idPressure) {
        pressureService.deletePressure(idPressure);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PressureResponseDTO>> findAllPressureByCurrentUser() {
        return ResponseEntity.ok().body(pressureService.findAllPressureByCurrentUser());
    }

    @GetMapping(value = "/{idPressure}")
    public ResponseEntity<PressureResponseDTO> findPressureById(@PathVariable Long idPressure) {
        return ResponseEntity.ok().body(pressureService.findPressureById(idPressure));
    }

    @PostMapping
    public ResponseEntity<PressureResponseDTO> registerPressure(@RequestBody PressureRegistrationDTO pressure) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pressureService.registerPressure(pressure));
    }

    @PutMapping(value = "/{idPressure}")
    public ResponseEntity<PressureResponseDTO> updatePressure(@RequestBody PressureRegistrationDTO updatedPressure, @PathVariable Long idPressure) {
        return ResponseEntity.ok().body(pressureService.updatePressure(updatedPressure, idPressure));
    }

    @GetMapping(value = "/find-by-date")
    public ResponseEntity<List<PressureResponseDTO>> getPressureByDate(
            @RequestParam LocalDateTime initialDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok().body(pressureService.getPressureByDate(initialDate, endDate));
    }

}
