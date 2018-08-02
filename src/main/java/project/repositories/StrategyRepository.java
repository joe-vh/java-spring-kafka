package project.repositories;

import project.models.Strategy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrategyRepository extends CrudRepository<Strategy, Long> {
    Strategy getOne(Long id);
    Optional<Strategy> findByIdAndUserId(Long id, Long userId);
    List<Strategy> findByUserIdAndOccurred(Long userId, Boolean occurred);
    List<Strategy> findByUserIdAndBasketIdAndOccurred(Long userId, Long basketId, Boolean occurred);
}
