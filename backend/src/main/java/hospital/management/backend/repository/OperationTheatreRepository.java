package hospital.management.backend.repository;

import hospital.management.backend.entity.OperationTheatre;
import hospital.management.backend.enums.TheatreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationTheatreRepository extends JpaRepository<OperationTheatre, Long> {
    Optional<OperationTheatre> findByName(String name);
    List<OperationTheatre> findByStatus(TheatreStatus status);
}
