package az.xazar.msvacation.mapper;

import az.xazar.msvacation.dao.entity.VacationEntity;
import az.xazar.msvacation.model.VacationDto;
import az.xazar.msvacation.model.VacationGetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface VacationMapper {

    VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);

    @Mapping(target = "file", ignore = true)
    VacationDto toVacationDto(VacationEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VacationEntity toVacationEntity(VacationDto dto);

    @Mapping(target = "userName", ignore = true)
    VacationGetDto toVacationGetDto(VacationEntity entity);
}
