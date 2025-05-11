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
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.BloodGlucoseRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ParametersRequiredException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ResourceNotFoundException;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    @DisplayName("findAllGlucoseByCurrentUser: deve retornar todos os registros de glicose do paciênte logado")
    void findAllGlucoseByCurrentUser() {
        BloodGlucose bloodGlucoseOne = new BloodGlucose();
        BloodGlucose bloodGlucoseTwo = new BloodGlucose();
        List<BloodGlucose> glucoseList = List.of(bloodGlucoseOne, bloodGlucoseTwo);
        BloodGlucoseResponseDTO dto = new BloodGlucoseResponseDTO();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.listAllGlucoseOfPatient(patientMock)).thenReturn(glucoseList);
        when(glucoseMapper.toResponseDTO(any(BloodGlucose.class))).thenReturn(dto);

        List<BloodGlucoseResponseDTO> result = glucoseService.findAllGlucoseByCurrentUser();

        verify(authenticatedUser).getCurrentUser();
        verify(bloodGlucoseRepository).listAllGlucoseOfPatient(patientMock);
        verify(glucoseMapper, times(2)).toResponseDTO(any(BloodGlucose.class));
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findGlucoseById: a glicose que tem o di passado deve ser retornada")
    void findGlucoseByIdCase1() {
        long idGlucose = 1;
        BloodGlucose bloodGlucose = new BloodGlucose();
        BloodGlucoseResponseDTO dto = new BloodGlucoseResponseDTO();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.of(bloodGlucose));
        when(glucoseMapper.toResponseDTO(bloodGlucose)).thenReturn(dto);

        BloodGlucoseResponseDTO result = glucoseService.findGlucoseById(idGlucose);

        verify(authenticatedUser).getCurrentUser();
        verify(bloodGlucoseRepository).findBloodGlucoseById(patientMock, idGlucose);
        verify(glucoseMapper).toResponseDTO(bloodGlucose);
        assertNotNull(result);
    }

    @Test
    @DisplayName("findGlucoseByIdCase: quando a glicose não é encontrada com o ID passado, uma exceção é lançada")
    void findGlucoseByIdCase2() {
        long idGlucose = 1;
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.empty());
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);

        assertThrows(ResourceNotFoundException.class, () -> glucoseService.findGlucoseById(idGlucose));
        verifyNoInteractions(glucoseMapper);
    }

    @Test
    @DisplayName("updateGlucose: quando a glicose é atualizada com sucesso")
    void updateGlucoseCase1() {
        long idGlucose = 1;
        BloodGlucose oldGlucose = new BloodGlucose();
        oldGlucose.setGlucoseValue(110);
        BloodGlucoseRegistrationDTO uptGlucose = new BloodGlucoseRegistrationDTO();
        uptGlucose.setGlucoseValue(100);

        BloodGlucoseResponseDTO dto = new BloodGlucoseResponseDTO();
        dto.setGlucoseValue(100);

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.of(oldGlucose));
        when(bloodGlucoseRepository.save(any(BloodGlucose.class))).thenReturn(oldGlucose);
        when(glucoseMapper.toResponseDTO(oldGlucose)).thenReturn(dto);

        BloodGlucoseResponseDTO result = glucoseService.updateGlucose(uptGlucose, idGlucose);

        verify(bloodGlucoseRepository).save(oldGlucose);
        verify(bloodGlucoseRepository).findBloodGlucoseById(patientMock, idGlucose);
        verify(glucoseMapper).toResponseDTO(oldGlucose);
        assertEquals(oldGlucose.getGlucoseValue(), dto.getGlucoseValue());
        assertNotNull(result);

    }

    @Test
    @DisplayName("updateGlucose: a glicose a ser atualizada não é encontrada, uma exceção é lançada")
    void updateGlucoseCase2() {
        long idGlucose = 1;

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.findBloodGlucoseById(patientMock, idGlucose)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> glucoseService.findGlucoseById(idGlucose));
        verifyNoInteractions(glucoseMapper);
    }

    @Test
    @DisplayName("getGlucoseByDate:")
    void getGlucoseByDateCase1() {
        LocalDateTime initialDate = LocalDateTime.of(2025, 05, 10, 8, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 05, 10, 8, 10);
        BloodGlucose bloodGlucoseOne = new BloodGlucose();
        BloodGlucose bloodGlucoseTwo = new BloodGlucose();
        bloodGlucoseOne.setMeasurementTime(LocalDateTime.of(2025, 05, 10, 8, 10));
        bloodGlucoseTwo.setMeasurementTime(LocalDateTime.of(2025, 05, 10, 8, 5));
        List<BloodGlucose> glucoseList = List.of(bloodGlucoseOne, bloodGlucoseTwo);
        BloodGlucoseResponseDTO dto = new BloodGlucoseResponseDTO();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(bloodGlucoseRepository.listGlucoseByDate(patientMock, initialDate, endDate)).thenReturn(glucoseList);
        when(glucoseMapper.toResponseDTO(any(BloodGlucose.class))).thenReturn(dto);

        List<BloodGlucoseResponseDTO> result = glucoseService.getGlucoseByDate(initialDate, endDate);

        assertNotNull(result);
        assertEquals(glucoseList.size(), result.size());
        verify(glucoseMapper, times(2)).toResponseDTO(any(BloodGlucose.class));
        verify(authenticatedUser).getCurrentUser();

    }

    @Test
    @DisplayName("getGlucoseByDate: quando um ou mais dos parametros de busca são nulos uma exceção é lançada")
    void getGlucoseByDateCase2() {
        assertThrows(ParametersRequiredException.class, () -> glucoseService.getGlucoseByDate(null, null));
        verifyNoInteractions(glucoseMapper);
        verifyNoInteractions(bloodGlucoseRepository);
    }


}
