package hospital.management.backend.service;

import hospital.management.backend.dto.response.PatientReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PatientReportService {
    PatientReportResponse upload(Long patientId, String title, MultipartFile file);
    List<PatientReportResponse> findByPatient(Long patientId);
    String createDownloadUrl(Long patientId, Long reportId);
}
