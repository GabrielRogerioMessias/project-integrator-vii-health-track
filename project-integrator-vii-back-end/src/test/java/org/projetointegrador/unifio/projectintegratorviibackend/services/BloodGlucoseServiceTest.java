package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.enums.MeasurementContext;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.mappers.GlucoseMapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.BloodGlucoseRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ResourceNotFoundException;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BloodGlucoseServiceTest {
    @Mock
    private BloodGlucoseRepository bloodGlucoseRepository;
    @Mock
    private AuthenticatedUser authenticatedUser;
    @Mock
    private Validator validator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GlucoseMapper glucoseMapper;
    @InjectMocks
    private BloodGlucoseService glucoseService;
    User userMock;
    Patient patientMock;

    @BeforeEach
    void setUpTests() {
        MockitoAnnotations.openMocks(this);
        userMock = new User();
        patientMock = new Patient();
        userMock.setPatient(patientMock);
        patientMock.setGlucoseList(new ArrayList<>());
        glucoseService = new BloodGlucoseService(bloodGlucoseRepository, authenticatedUser, validator, userRepository, glucoseMapper);
    }

    @Test
    @DisplayName("registerGlucose: registrar nova glicose com sucesso quando os dados estão válidos")
    void registerBloodGlucoseCase1() {
        //arrange
        BloodGlucoseRegistrationDTO dto = new BloodGlucoseRegistrationDTO();
        dto.setGlucoseValue(120);
        dto.setContext(MeasurementContext.JEJUM);
        dto.setMeasurementTime(LocalDateTime.now());

        BloodGlucose savedEntity = new BloodGlucose();
        savedEntity.setContext(dto.getContext());
        savedEntity.setGlucoseValue(dto.getGlucoseValue());
        savedEntity.setMeasurementTime(dto.getMeasurementTime());

        BloodGlucoseResponseDTO responseDTO = new BloodGlucoseResponseDTO();
        responseDTO.setGlucoseValue(savedEntity.getGlucoseValue());
        responseDTO.setContext(savedEntity.getContext());

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.save(any(BloodGlucose.class))).thenReturn(savedEntity);
        when(glucoseMapper.toResponseDTO(any())).thenReturn(responseDTO);

        BloodGlucoseResponseDTO result = glucoseService.registerGlucose(dto);

        // asserts and verify
        assertNotNull(result);
        verify(userRepository).save(userMock);
        verify(userRepository, times(1)).save(userMock);
        verify(bloodGlucoseRepository, times(1)).save(any(BloodGlucose.class));
    }

    @Test
    @DisplayName("registerBloodGlucoseCase: falha ao tentar registrar uma nova glicose com dados inválidos ")
    void registerBloodGlucoseCase2() {
        BloodGlucoseRegistrationDTO dto = new BloodGlucoseRegistrationDTO();
        ConstraintViolation<BloodGlucoseRegistrationDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("glucoseValue of Glucose may not blank or null");
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        assertThrows(NullEntityFieldException.class, () -> glucoseService.registerGlucose(dto));
        verifyNoInteractions(bloodGlucoseRepository);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteGlucoseCase1() {
        long idGlucose = 1L;
        BloodGlucose glucose = new BloodGlucose();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.of(glucose));
        doNothing().when(bloodGlucoseRepository).delete(glucose);

        glucoseService.deleteGlucose(idGlucose);

        verify(bloodGlucoseRepository).delete(glucose);
        verify(authenticatedUser).getCurrentUser();
        verify(userRepository).save(userMock);
    }

    @Test
    @DisplayName("deleteGlucose: a glicose a ser deletada não é encontrada, uma exceção deve ser lançada")
    void deleteGlucoseCase2() {
        long idGlucose = 1L;
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> glucoseService.findGlucoseById(idGlucose));

        verifyNoInteractions(glucoseMapper);
        verify(bloodGlucoseRepository, never()).delete(any(BloodGlucose.class));
        verify(userRepository, never()).save(userMock);
    }

}
