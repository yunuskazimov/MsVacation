package az.xazar.msvacation.util;

import az.xazar.msvacation.entity.VacationEntity;
import az.xazar.msvacation.error.ErrorCodes;
import az.xazar.msvacation.exception.VacationNotFoundException;
import az.xazar.msvacation.repository.VacationRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class VacationUtil {
    private final VacationRepo vacationRepo;

    public VacationUtil(VacationRepo vacationRepo) {
        this.vacationRepo = vacationRepo;
    }

    public VacationEntity findVacationById(Long id) {
        return vacationRepo.findById(id)
                .orElseThrow(() -> new VacationNotFoundException(ErrorCodes.NOT_FOUND));
    }

    public List<VacationEntity> findVacationListByUserId(Long userId) {
        return vacationRepo.findAllByUserId(userId)
                .orElseThrow(() -> new VacationNotFoundException(ErrorCodes.NOT_FOUND));
    }

    public VacationEntity findVacationByUserId(Long userId) {
        return vacationRepo.findByUserId(userId)
                .orElseThrow(() -> new VacationNotFoundException(ErrorCodes.NOT_FOUND));
    }
}
