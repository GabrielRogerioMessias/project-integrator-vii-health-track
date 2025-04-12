package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.projetointegrador.unifio.projectintegratorviibackend.models.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.BloodGlucoseRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ResourceNotFoundException;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BloodGlucoseService {
    private final BloodGlucoseRepository bloodGlucoseRepository;
    private final AuthenticatedUser authenticatedUser;
    private final Validator validator;
    private final UserRepository userRepository;

    public BloodGlucoseService(BloodGlucoseRepository bloodGlucoseRepository, AuthenticatedUser authenticatedUser, Validator validator, UserRepository userRepository) {
        this.bloodGlucoseRepository = bloodGlucoseRepository;
        this.authenticatedUser = authenticatedUser;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public void deleteGlucose(Long idGlucose) {
        User loggedUser = authenticatedUser.getCurrentUser();
        BloodGlucose glucoseToDelete = bloodGlucoseRepository.findBloodGlucoseById(loggedUser.getPatient(), idGlucose).orElseThrow(() -> new ResourceNotFoundException(BloodGlucose.class, Long.toString(idGlucose)));
        loggedUser.getPatient().getGlucoseList().remove(glucoseToDelete);
        userRepository.save(loggedUser);
        bloodGlucoseRepository.delete(glucoseToDelete);
    }

    public List<BloodGlucoseResponseDTO> findAllGlucoseByCurrentUser() {
        User loggedUser = authenticatedUser.getCurrentUser();
        List<BloodGlucose> glucoseList = bloodGlucoseRepository.listAllGlucoseOfPatient(loggedUser.getPatient());
        List<BloodGlucoseResponseDTO> responseDTOList = new ArrayList<>();
        for (BloodGlucose glucose : glucoseList) {
            responseDTOList.add(new BloodGlucoseResponseDTO(glucose));
        }
        return responseDTOList;
    }

    public BloodGlucoseResponseDTO findGlucoseById(Long idGlucose) {
        User loggedUser = authenticatedUser.getCurrentUser();
        BloodGlucose bloodGlucose = bloodGlucoseRepository.findBloodGlucoseById(loggedUser.getPatient(), idGlucose).orElseThrow(() -> new ResourceNotFoundException(BloodGlucose.class, Long.toString(idGlucose)));
        return new BloodGlucoseResponseDTO(bloodGlucose);
    }

    public BloodGlucoseResponseDTO registerGlucose(BloodGlucoseRegistrationDTO bloodGlucose) {
        User loggedUser = authenticatedUser.getCurrentUser();
        List<String> errors;
        errors = validFields(bloodGlucose);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        BloodGlucose newBloodGlucose = new BloodGlucose();
        newBloodGlucose.setGlucoseValue(bloodGlucose.getGlucoseValue());
        newBloodGlucose.setPatient(loggedUser.getPatient());
        newBloodGlucose.setContext(bloodGlucose.getContext());
        newBloodGlucose.setMeasurementTime(bloodGlucose.getMeasurementTime());

        loggedUser.getPatient().getGlucoseList().add(newBloodGlucose);
        userRepository.save(loggedUser);
        return new BloodGlucoseResponseDTO(bloodGlucoseRepository.save(newBloodGlucose));
    }

    public BloodGlucoseResponseDTO updateGlucose(BloodGlucoseRegistrationDTO glucoseUpdated, Long idGlucose) {
        User loggedUser = authenticatedUser.getCurrentUser();
        BloodGlucose oldBloodGlucose = bloodGlucoseRepository.findBloodGlucoseById(loggedUser.getPatient(), idGlucose).orElseThrow(() -> new ResourceNotFoundException(BloodGlucose.class, Long.toString(idGlucose)));
        this.updateFields(oldBloodGlucose, glucoseUpdated);
        bloodGlucoseRepository.save(oldBloodGlucose);
        return new BloodGlucoseResponseDTO(oldBloodGlucose);
    }

    //todo
    private void updateFields(BloodGlucose oldGlucose, BloodGlucoseRegistrationDTO updatedGlucose) {
        if (updatedGlucose.getMeasurementTime() != null) {
            oldGlucose.setMeasurementTime(updatedGlucose.getMeasurementTime());
        }
        if (updatedGlucose.getGlucoseValue() != null) {
            oldGlucose.setGlucoseValue(updatedGlucose.getGlucoseValue());
        }
        if (updatedGlucose.getContext() != null) {
            oldGlucose.setContext(updatedGlucose.getContext());
        }
    }

    public List<BloodGlucoseResponseDTO> getGlucoseByDate(LocalDateTime initialDate, LocalDateTime endDate) {
        User loggedUser = authenticatedUser.getCurrentUser();
        List<BloodGlucose> glucoseList = bloodGlucoseRepository.listGlucoseByDate(loggedUser.getPatient(), initialDate, endDate);
        List<BloodGlucoseResponseDTO> glucoseResponseList = new ArrayList<>();
        for (BloodGlucose glucose : glucoseList) {
            glucoseResponseList.add(new BloodGlucoseResponseDTO(glucose));
        }
        return glucoseResponseList;
    }

    private <T> List<String> validFields(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }


}
