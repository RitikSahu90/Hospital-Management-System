package hospital.management.backend.controller;

import hospital.management.backend.dto.request.MedicalRecordRequest;
import hospital.management.backend.dto.response.MedicalRecordResponse;
import hospital.management.backend.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> create(@Valid @RequestBody MedicalRecordRequest request) { MedicalRecordResponse response = service.create(request); return ResponseEntity.created(URI.create("/api/medical-records/" + response.getId())).body(response); }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public List<MedicalRecordResponse> list() { return service.findAll(); }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public MedicalRecordResponse get(@PathVariable Long id) { return service.findById(id); }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public MedicalRecordResponse update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}