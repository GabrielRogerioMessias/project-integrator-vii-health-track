package org.projetointegrador.unifio.projectintegratorviibackend.models.mappers;

import org.mapstruct.Mapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.Patient;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientResponseDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.patient.PatientUpdateDTO;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    // PatientRegisterDTO -> Patient
    Patient toEntity(PatientRegistrationDTO dto);

    // Patient -> PatientResponseDTO
    PatientResponseDTO toResponseDTO(Patient patient);

    // PatientUpdateDTO -> PatientResponseDTO
    PatientResponseDTO updateToResponseDTO(PatientUpdateDTO dto);
}
