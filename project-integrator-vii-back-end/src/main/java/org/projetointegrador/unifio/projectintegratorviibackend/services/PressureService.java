package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.projetointegrador.unifio.projectintegratorviibackend.models.mappers.PressureMapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.PressureRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ParametersRequiredException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ResourceNotFoundException;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PressureService {

    private final PressureRepository pressureRepository;
    private final AuthenticatedUser authenticatedUser;
    private final Validator validator;
    private final UserRepository userRepository;
    private final PressureMapper pressureMapper;

    public PressureService(PressureRepository pressureRepository, AuthenticatedUser authenticatedUser, Validator validator, UserRepository userRepository, PressureMapper pressureMapper) {
        this.pressureRepository = pressureRepository;
        this.authenticatedUser = authenticatedUser;
        this.validator = validator;
        this.userRepository = userRepository;
        this.pressureMapper = pressureMapper;
    }


    public void deletePressure(Long idPressure) {
        User loggedUser = authenticatedUser.getCurrentUser();
        Pressure pressureToDelete = pressureRepository.findPressureById(loggedUser.getPatient(), idPressure).orElseThrow(() -> new ResourceNotFoundException(Pressure.class, Long.toString(idPressure)));
        pressureRepository.delete(pressureToDelete);
    }

    public List<PressureResponseDTO> findAllPressureByCurrentUser() {
        User loggedUser = authenticatedUser.getCurrentUser();
        List<Pressure> pressureList = pressureRepository.listAllPressureOfPatient(loggedUser.getPatient());
        return pressureList
                .stream()
                .map(pressureMapper::toResponseDTO)
                .toList();
    }

    public PressureResponseDTO findPressureById(Long idPressure) {
        User loggedUser = authenticatedUser.getCurrentUser();
        Pressure pressure = pressureRepository.findPressureById(loggedUser.getPatient(), idPressure).orElseThrow(() -> new ResourceNotFoundException(Pressure.class, Long.toString(idPressure)));
        return pressureMapper.toResponseDTO(pressure);
    }

    public PressureResponseDTO registerPressure(PressureRegistrationDTO pressure) {
        User loggedUser = authenticatedUser.getCurrentUser();
        List<String> errors = this.validateFields(pressure);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        Pressure newPressure = new Pressure();
        newPressure.setSystolic(pressure.getSystolic());
        newPressure.setHeartbeat(pressure.getHeartbeat());
        newPressure.setMeasurementTime(pressure.getMeasurementTime());
        newPressure.setPatient(loggedUser.getPatient());
        newPressure.setDiastolic(pressure.getDiastolic());
        loggedUser.getPatient().getPressureList().add(newPressure);
        userRepository.save(loggedUser);
        return pressureMapper.toResponseDTO(pressureRepository.save(newPressure));
    }

    public PressureResponseDTO updatePressure(PressureRegistrationDTO updatedPressure, Long idPressure) {
        User loggedUser = authenticatedUser.getCurrentUser();
        Pressure oldPressure = pressureRepository.findPressureById(loggedUser.getPatient(), idPressure).orElseThrow(() -> new ResourceNotFoundException(Pressure.class, Long.toString(idPressure)));
        this.updateFields(oldPressure, updatedPressure);
        return pressureMapper.toResponseDTO(pressureRepository.save(oldPressure));
    }

    public List<PressureResponseDTO> getPressureByDate(LocalDateTime initialDate, LocalDateTime endDate) {
        User loggedUser = authenticatedUser.getCurrentUser();
        if (initialDate == null || endDate == null) {
            throw new ParametersRequiredException("Parameters initial date or end date cannot be null or empty.");
        }
        List<Pressure> pressureList = pressureRepository.listPressureByDate(loggedUser.getPatient(), initialDate, endDate);
        return pressureList
                .stream()
                .map(pressureMapper::toResponseDTO)
                .toList();
    }

    private <T> List<String> validateFields(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }

    private void updateFields(Pressure oldPressure, PressureRegistrationDTO updatedPressure) {
        if (updatedPressure.getDiastolic() != null) {
            oldPressure.setDiastolic(updatedPressure.getDiastolic());
        }
        if (updatedPressure.getHeartbeat() != null) {
            oldPressure.setHeartbeat(updatedPressure.getHeartbeat());
        }
        if (updatedPressure.getMeasurementTime() != null) {
            oldPressure.setMeasurementTime(updatedPressure.getMeasurementTime());
        }
        if (updatedPressure.getSystolic() != null) {
            oldPressure.setSystolic(updatedPressure.getSystolic());
        }
    }
}
