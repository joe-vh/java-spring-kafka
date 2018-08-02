package project.repositories;

import project.models.Condition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends CrudRepository<Condition, Long> {
    List<Condition> findByUserIdAndOccurred(Long id, Boolean occurred);
    Iterable<Condition> findByStrategyId(Long id);
    Iterable<Condition> findByFutureId(Long futureId);
    Iterable<Condition> findByFutureIdAndOccurred(Long futureId, Boolean occurred);
    Iterable<Condition> findByOptionId(Long optionId);
    Iterable<Condition> findByOptionIdAndOccurred(Long optionId, Boolean occurred);
    Iterable<Condition> findByCfdId(Long cfdId);
    Iterable<Condition> findByCfdIdAndOccurred(Long cfdId, Boolean occurred);
}

