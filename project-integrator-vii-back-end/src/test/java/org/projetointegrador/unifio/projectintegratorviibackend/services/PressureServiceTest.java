package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.mappers.PressureMapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.PressureRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.ResourceNotFoundException;
import org.projetointegrador.unifio.projectintegratorviibackend.utils.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PressureServiceTest {
    @Mock
    private PressureRepository pressureRepository;
    @Mock
    private AuthenticatedUser authenticatedUser;
    @Mock
    private Validator validator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PressureMapper pressureMapper;
    @InjectMocks
    PressureService pressureService;
    User userMock;
    Patient patientMock;

    @BeforeEach
    void setUpTests() {
        MockitoAnnotations.openMocks(this);
        userMock = new User();
        patientMock = new Patient();
        userMock.setPatient(patientMock);
        patientMock.setPressureList(new ArrayList<>());
        pressureService = new PressureService(pressureRepository, authenticatedUser, validator, userRepository, pressureMapper);
    }

    @Test
    @DisplayName("registerPressure: registrar nova pressão com sucesso quando dados estão válidos")
    void registerPressureCase1() {
        // arrange
        PressureRegistrationDTO dto = new PressureRegistrationDTO();
        dto.setDiastolic(110);
        dto.setSystolic(100);
        dto.setHeartbeat(90);
        dto.setMeasurementTime(LocalDateTime.now());

        Pressure savedPressure = new Pressure();
        savedPressure.setDiastolic(dto.getDiastolic());
        savedPressure.setSystolic(dto.getSystolic());
        savedPressure.setHeartbeat(dto.getHeartbeat());
        savedPressure.setMeasurementTime(dto.getMeasurementTime());
        when(pressureRepository.save(any(Pressure.class))).thenReturn(savedPressure);

        PressureResponseDTO responseDTO = new PressureResponseDTO();
        responseDTO.setDiastolic(savedPressure.getDiastolic());
        when(pressureMapper.toResponseDTO(savedPressure)).thenReturn(responseDTO);

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);

        PressureResponseDTO response = pressureService.registerPressure(dto);

        assertNotNull(response);
        verify(userRepository, times(1)).save(userMock);
        verify(pressureRepository, times(1)).save(any(Pressure.class));
        verify(pressureMapper).toResponseDTO(savedPressure);
        verify(validator).validate(dto);
    }

    @Test
    @DisplayName("registerPressure: falha ao tentar registrar pressão com dados inválidos")
    void registerPressureCase2() {
        PressureRegistrationDTO dto = new PressureRegistrationDTO();
        ConstraintViolation<PressureRegistrationDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Systolic of Pressure may not blank");
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        // execute and wait an exception
        assertThrows(NullEntityFieldException.class, () -> pressureService.registerPressure(dto));

        verify(userRepository, never()).save(any(User.class));
        verify(pressureRepository, never()).save(any(Pressure.class));
    }

    @Test
    @DisplayName("findPressureById: a pressão que tem o id passado deve ser retornada")
    void findPressureByIdCase1() {
        long idPressure = 1;
        Pressure pressure = new Pressure();
        pressure.setId(idPressure);

        PressureResponseDTO response = new PressureResponseDTO();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.of(pressure));
        when(pressureMapper.toResponseDTO(pressure)).thenReturn(response);
        PressureResponseDTO result = pressureService.findPressureById(idPressure);

        assertNotNull(result);
        verify(pressureRepository, times(1)).findPressureById(patientMock, idPressure);
    }

    @Test
    @DisplayName("findPressureById: quando a pressão não é encontrada com o ID passado e a exceção ResourceNotFoundException é lançada")
    void findPressureByIdCase2() {
        long idPressure = 1;
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pressureService.findPressureById(idPressure));
    }

    @Test
    @DisplayName("deletePressure: a pressão deve ser deletada com sucesso")
    void deletePressureCase1() {
        long idPressure = 1;
        Pressure pressure = new Pressure();
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.of(pressure));
        doNothing().when(pressureRepository).delete(pressure);
        pressureService.deletePressure(idPressure);
        verify(pressureRepository, times(1)).delete(pressure);
    }

    @Test
    @DisplayName("deletePressure: a pressão a ser deletada não é encontrada, a exceção ResourceNotFoundException deve ser lançada")
    void deletePressureCase2() {
        long idPressure = 1;
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pressureService.deletePressure(idPressure));
        verify(pressureRepository, never()).delete(any(Pressure.class));
        verify(pressureRepository, times(1)).findPressureById(patientMock, idPressure);
    }

    @Test
    @DisplayName("findAllPressureByCurrentUser: deve retornar todos os registro de pressão do paciente logado")
    void findAllPressureByCurrentUser() {
        Pressure pressureOne = new Pressure();
        Pressure pressureTwo = new Pressure();

        List<Pressure> pressureList = List.of(pressureTwo, pressureOne);

        PressureResponseDTO dto = new PressureResponseDTO();

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.listAllPressureOfPatient(patientMock)).thenReturn(pressureList);
        when(pressureMapper.toResponseDTO(any(Pressure.class))).thenReturn(dto);

        List<PressureResponseDTO> result = pressureService.findAllPressureByCurrentUser();
        verify(authenticatedUser, times(1)).getCurrentUser();
        verify(pressureRepository, times(1)).listAllPressureOfPatient(patientMock);
        verify(pressureMapper, times(2)).toResponseDTO(any(Pressure.class));
        assertNotNull(result, "Resultado não deve ser nulo");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("updatePressure: a pressão é atualizada com sucesso")
    void updatePressureCase1() {
        long idPressure = 1;
        Pressure oldPressure = new Pressure();
        oldPressure.setId(idPressure);
        oldPressure.setSystolic(100);

        PressureRegistrationDTO uptPressure = new PressureRegistrationDTO();
        uptPressure.setSystolic(110);

        PressureResponseDTO dto = new PressureResponseDTO();
        dto.setSystolic(110);

        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.of(oldPressure));
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.save(any(Pressure.class))).thenReturn(oldPressure);
        when(pressureMapper.toResponseDTO(oldPressure)).thenReturn(dto);

        PressureResponseDTO result = pressureService.updatePressure(uptPressure, idPressure);

        assertEquals(uptPressure.getSystolic(), result.getSystolic());
        verify(pressureRepository,times(1)).save(oldPressure);
        verify(pressureRepository, times(1)).findPressureById(patientMock, idPressure);
    }

    @Test
    @DisplayName("updatePressure: a pressão a ser atualizada não é encontrada, a exceção ResourceNotFoundException deve ser lançada")
    void updatePressureCase2() {
        long idPressure = 1;
        PressureRegistrationDTO upt = new PressureRegistrationDTO();
        when(authenticatedUser.getCurrentUser()).thenReturn(userMock);
        when(pressureRepository.findPressureById(patientMock, idPressure)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pressureService.updatePressure(upt,idPressure));
        verify(pressureRepository, never()).save(any(Pressure.class));
        verify(pressureRepository, times(1)).findPressureById(patientMock, idPressure);
    }
}
