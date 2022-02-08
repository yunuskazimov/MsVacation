package az.xazar.msvacation.dao.repository;

import az.xazar.msvacation.dao.entity.VacationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationRepo extends PagingAndSortingRepository<VacationEntity, Long> {
    Optional<Page<VacationEntity>> findAllByUserId(Long userId, Pageable pageable);

    Page<VacationEntity> findByDeletedFalse(Pageable pageable);

}
