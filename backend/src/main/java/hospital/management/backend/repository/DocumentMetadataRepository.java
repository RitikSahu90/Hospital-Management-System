package hospital.management.backend.repository;

import hospital.management.backend.entity.DocumentMetadata;
import hospital.management.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, Long> {
    List<DocumentMetadata> findByPatient(Patient patient);
    List<DocumentMetadata> findByPatientAndFileType(Patient patient, String fileType);
}
