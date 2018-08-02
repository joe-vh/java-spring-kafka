package project.repositories;

import project.models.OptionTick;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionTickRepository extends CrudRepository<OptionTick, Long> {
    List<OptionTick> findByOptionId(Long id);
    @Query("select t from OptionTick t where t.timestamp >= CURRENT_DATE")
    List<OptionTick> findAllToday();
    Optional<OptionTick> findFirstByOptionIdOrderByTimestampDesc(Long id);
}
