package project.repositories;

import project.models.CfdTick;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CfdTickRepository extends CrudRepository<CfdTick, Long> {
    List<CfdTick> findByCfdId(Long cfdId);
    Optional<CfdTick> findFirstByCfdIdOrderByTimestampDesc(Long id);
    @Query("select t from CfdTick t where t.timestamp >= CURRENT_DATE")
    List<CfdTick> findAllToday();
}
