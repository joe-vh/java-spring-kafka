package project.repositories;

import project.models.Cfd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CfdRepository extends JpaRepository<Cfd, Long> {}
