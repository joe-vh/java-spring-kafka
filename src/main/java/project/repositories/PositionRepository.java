package project.repositories;

import project.models.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {
    List<Position> findByUserIdAndQuantityNot(Long id, Integer quantity);
    List<Position> findByUserIdAndBasketIdAndQuantityNot(Long userId, Long basketId, Integer quantity);
    Iterable<Position> findByUserIdAndBasketIdAndFutureId(Long userId, Long basketId, Long futureId);
    Iterable<Position> findByUserIdAndBasketIdAndOptionId(Long userId, Long basketId, Long optionId);
    Iterable<Position> findByUserIdAndBasketIdAndCfdIdAndSide(Long userId, Long basketId, Long cfdId, Boolean Side);
    Optional<Position> findByIdAndUserId(Long id, Long userId);
}

