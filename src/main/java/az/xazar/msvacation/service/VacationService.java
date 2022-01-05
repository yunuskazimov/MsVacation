package az.xazar.msvacation.service;

import az.xazar.msvacation.model.VacationDto;

import java.util.List;

public interface VacationService {
    VacationDto create(VacationDto vacationDto);

    VacationDto edit(Long id, VacationDto vacationDto);

    String delete(Long id);

    VacationDto getById(Long id);

    String getFileUrlById(Long id);

    List<VacationDto> getList();

    List<VacationDto> getByUserId(Long userid);
}
