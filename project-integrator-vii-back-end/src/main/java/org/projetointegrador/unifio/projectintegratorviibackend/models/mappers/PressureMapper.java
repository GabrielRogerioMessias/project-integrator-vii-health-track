package org.projetointegrador.unifio.projectintegratorviibackend.models.mappers;

import org.mapstruct.Mapper;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.PressureResponseDTO;

@Mapper(componentModel = "spring")
public interface PressureMapper {

    //    PressureResponseDTO -> pressure
    Pressure toEntity(PressureResponseDTO dto);

    //    Pressure -> PressureResponseDTO
    PressureResponseDTO toResponseDTO(Pressure pressure);
}
