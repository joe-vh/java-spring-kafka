package project.repositories;

import project.models.Signal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignalRepository extends CrudRepository<Signal, Long>  {
    Optional<Signal> findById(Long id);
    List<Signal> findByUserId(Long id);
}
