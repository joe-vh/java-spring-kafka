package project.repositories;

import project.models.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Long> {
    List<Basket> findByUserId(Long id);
    Optional<Basket> findByIdAndUserId(Long id, Long userId);
}

