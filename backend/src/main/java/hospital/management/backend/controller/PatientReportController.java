package hospital.management.backend.controller;

import hospital.management.backend.dto.response.PatientReportResponse;
import hospital.management.backend.service.PatientReportService;
import hospital.management.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/reports")
@RequiredArgsConstructor
public class PatientReportController {
    private final PatientReportService reportService;
    private final PatientService patientService;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','PHARMACIST')")
    public ResponseEntity<PatientReportResponse> upload(@PathVariable Long patientId,
                                                @RequestParam String title,
                                                @RequestPart MultipartFile file,
                                                Authentication authentication) {
        ensureOwnership(patientId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.upload(patientId, title, file));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','PHARMACIST')")
    public ResponseEntity<List<PatientReportResponse>> list(@PathVariable Long patientId, Authentication authentication) {
        ensureOwnership(patientId, authentication);
        return ResponseEntity.ok(reportService.findByPatient(patientId));
    }

    @GetMapping("/report/{reportId}/download")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','PHARMACIST')")
    public ResponseEntity<java.util.Map<String, String>> download(@PathVariable Long patientId, @PathVariable Long reportId, Authentication authentication) {
        ensureOwnership(patientId, authentication);
        return ResponseEntity.ok(java.util.Map.of("url", reportService.createDownloadUrl(patientId, reportId)));
    }

    private void ensureOwnership(Long patientId, Authentication authentication) {
        boolean patient = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));
        if (patient && !patientService.belongsToUser(patientId, authentication.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Forbidden");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
