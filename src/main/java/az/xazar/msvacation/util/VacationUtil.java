package az.xazar.msvacation.util;

import az.xazar.msvacation.dao.entity.VacationEntity;
import az.xazar.msvacation.dao.repository.VacationRepo;
import az.xazar.msvacation.exception.ErrorCodes;
import az.xazar.msvacation.exception.VacationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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

    public Page<VacationEntity> findVacationListByUserId(Long userId, Pageable pageable) {
        Page<VacationEntity> entityList = vacationRepo.findAllByUserId(userId, pageable).get();
        if (entityList.isEmpty()) {
            throw new VacationNotFoundException(ErrorCodes.NOT_FOUND);
        } else {
            return entityList;
        }
    }

//    public void findVacationByUserId(Long userId) {
//        vacationRepo.findByUserId(userId)
//                .orElseThrow(() -> new VacationNotFoundException(ErrorCodes.NOT_FOUND));
//    }
}
