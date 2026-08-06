package hospital.management.backend.service;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse create(PrescriptionRequest request);
    PrescriptionResponse update(Long id, PrescriptionRequest request);
    void delete(Long id);
    PrescriptionResponse findById(Long id);
    List<PrescriptionResponse> findAll();
    List<PrescriptionResponse> findAllForPatient(String patientUsername);
    PrescriptionResponse uploadPdf(Long id, org.springframework.web.multipart.MultipartFile file);
    String createDownloadUrl(Long id);
}
