package hospital.management.backend.service;

import hospital.management.backend.dto.response.DocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface DocumentMetadataService {
    DocumentResponseDto upload(Long patientId, String fileType, String documentName, MultipartFile file, String uploadedBy);
    List<DocumentResponseDto> findAllForPatient(String patientUsername);
    List<DocumentResponseDto> findByPatientAndType(String patientUsername, String fileType);
}
