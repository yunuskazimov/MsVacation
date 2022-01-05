package az.xazar.msvacation.repository;

import az.xazar.msvacation.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRepo extends JpaRepository<VacationEntity,Long> {
    Optional<List<VacationEntity>> findAllByUserId(Long userId);
    Optional<VacationEntity> findByUserId(Long userId);

}
