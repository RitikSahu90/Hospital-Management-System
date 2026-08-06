package hospital.management.backend.controller;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<?>> getPatients(Authentication authentication) {
        if (authentication != null) {
            boolean isPatient = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));
            if (isPatient) {
                return ResponseEntity.ok(List.of(patientService.findForUsername(authentication.getName())));
            }
            boolean isDoctor = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"));
            if (isDoctor) {
                return ResponseEntity.ok(patientService.findAllForDoctor(authentication.getName()));
            }
        }
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPatient(@PathVariable Long id, Authentication authentication) {
        boolean patient = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));
        if (patient && !patientService.belongsToUser(id, authentication.getName())) return ResponseEntity.status(403).body(java.util.Map.of("error", "Forbidden"));
        return ResponseEntity.ok(patientService.findAll().stream().filter(item -> item.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Patient not found")));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.update(id, patient));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
