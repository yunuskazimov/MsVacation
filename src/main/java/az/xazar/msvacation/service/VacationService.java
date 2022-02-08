package az.xazar.msvacation.service;

import az.xazar.msvacation.model.PageDto;
import az.xazar.msvacation.model.VacationDto;
import az.xazar.msvacation.model.VacationGetDto;
import org.springframework.data.domain.Page;

public interface VacationService {
    VacationDto create(VacationDto vacationDto);

    VacationDto edit(Long id, VacationDto vacationDto);

    VacationDto editResult(Long id, String result);

    String delete(Long id);

    VacationDto getById(Long id);

    String getFileUrlById(Long id);

    Page<VacationGetDto> getList(PageDto page);

    Page<VacationGetDto> getByUserId(Long userid, PageDto page);
}
