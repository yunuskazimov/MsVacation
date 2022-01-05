package az.xazar.msvacation.mapper;

import az.xazar.msvacation.entity.VacationEntity;
import az.xazar.msvacation.model.VacationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface VacationMapper {

    VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);

    @Mapping(target = "file", ignore = true)
    @Mapping(target = "isDeleted", source = "deleted")
    VacationDto toBusinessTripDto(VacationEntity entity);

    List<VacationDto> toBusinessTripDtoList(List<VacationEntity> entities);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VacationEntity toBusinessTripEntity(VacationDto dto);
}
