package hospital.management.backend.controller;

import hospital.management.backend.dto.request.AvailabilityRequest;
import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.AvailabilityResponse;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        DoctorResponse resp = doctorService.create(request);
        return ResponseEntity.created(URI.create("/api/doctors/" + resp.getId())).body(resp);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','PHARMACIST')")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT','PHARMACIST')")
    public ResponseEntity<List<DoctorResponse>> listDoctors() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @PostMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<AvailabilityResponse> addAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityRequest request) {
        AvailabilityResponse resp = doctorService.addAvailability(id, request);
        return ResponseEntity.created(URI.create("/api/doctors/" + id + "/availability/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<AvailabilityResponse>> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getAvailability(id));
    }

    @PutMapping("/{id}/availability/{availabilityId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<AvailabilityResponse> updateAvailability(@PathVariable Long id, @PathVariable Long availabilityId, @Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(doctorService.updateAvailability(id, availabilityId, request));
    }

    @DeleteMapping("/{id}/availability/{availabilityId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id, @PathVariable Long availabilityId) {
        doctorService.deleteAvailability(id, availabilityId);
        return ResponseEntity.noContent().build();
    }
}
