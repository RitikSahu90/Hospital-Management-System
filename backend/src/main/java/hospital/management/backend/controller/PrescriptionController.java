package hospital.management.backend.controller;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST')")
    public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        PrescriptionResponse response = prescriptionService.create(request);
        return ResponseEntity.created(URI.create("/api/prescriptions/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST')")
    public ResponseEntity<PrescriptionResponse> updatePrescription(@PathVariable Long id, @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST','PATIENT')")
    public ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST')")
    public ResponseEntity<List<PrescriptionResponse>> listPrescriptions() {
        return ResponseEntity.ok(prescriptionService.findAll());
    }
}
