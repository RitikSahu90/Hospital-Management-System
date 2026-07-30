package hospital.management.backend.controller;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<?>> getPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(patient));
    }
}
