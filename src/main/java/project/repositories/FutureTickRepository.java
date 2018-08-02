package project.repositories;

import project.models.FutureTick;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FutureTickRepository extends CrudRepository<FutureTick, Long> {
    List<FutureTick> findByFutureId(Long id);
    Optional<FutureTick> findFirstByFutureIdOrderByTimestampDesc(Long id);
    @Query("select t from FutureTick t where t.timestamp >= CURRENT_DATE")
    List<FutureTick> findAllToday();
}
