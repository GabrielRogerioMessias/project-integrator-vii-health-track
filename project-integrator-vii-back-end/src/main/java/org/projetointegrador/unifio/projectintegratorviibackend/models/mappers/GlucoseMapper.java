package org.projetointegrador.unifio.projectintegratorviibackend.models.mappers;

import org.mapstruct.Mapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseRegistrationDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucoseResponseDTO;

@Mapper(componentModel = "spring")
public interface GlucoseMapper {

    //    BloodGlucoseRegistrationDTO -> BloodGlucose
    BloodGlucose toEntity(BloodGlucoseRegistrationDTO dto);

    //    BloodGlucose -> BloodGlucoseResponseDTO
    BloodGlucoseResponseDTO toResponseDTO(BloodGlucose bloodGlucose);
}
