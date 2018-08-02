package project.repositories;

import project.models.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FutureRepository extends JpaRepository<Future, Long> {}
