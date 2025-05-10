package org.projetointegrador.unifio.projectintegratorviibackend.services;

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
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @BeforeEach
    void setUpTests() {
        MockitoAnnotations.openMocks(this);
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

        User userMock = new User();
        Patient patientMock = new Patient();

        userMock.setPatient(patientMock);
        patientMock.setGlucoseList(new ArrayList<>());

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
    @DisplayName("when an new blood glucose not is successfully registered, ant an NullEntityFieldException is throwed")
    void shouldRegisterNewBloodGlucoseNotSuccessfully() {
    }


}
