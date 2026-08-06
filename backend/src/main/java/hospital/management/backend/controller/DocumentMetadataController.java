package hospital.management.backend.controller;

import hospital.management.backend.dto.response.DocumentResponseDto;
import hospital.management.backend.service.DocumentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentMetadataController {
    private final DocumentMetadataService documentMetadataService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST','RECEPTIONIST')")
    public ResponseEntity<DocumentResponseDto> uploadDocument(
            @RequestParam("patientId") Long patientId,
            @RequestParam("fileType") String fileType,
            @RequestParam("documentName") String documentName,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {
        String uploadedBy = authentication != null ? authentication.getName() : "SYSTEM";
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentMetadataService.upload(patientId, fileType, documentName, file, uploadedBy));
    }

    @GetMapping("/patient")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST','PATIENT')")
    public ResponseEntity<List<DocumentResponseDto>> getPatientDocuments(
            @RequestParam(value = "fileType", required = false) String fileType,
            Authentication authentication) {
        String patientUsername = authentication.getName();
        if (fileType != null && !fileType.isBlank()) {
            return ResponseEntity.ok(documentMetadataService.findByPatientAndType(patientUsername, fileType));
        }
        return ResponseEntity.ok(documentMetadataService.findAllForPatient(patientUsername));
    }
}
