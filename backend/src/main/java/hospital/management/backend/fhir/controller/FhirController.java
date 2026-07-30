package hospital.management.backend.fhir.controller;

import hospital.management.backend.fhir.dto.AppointmentFhirResource;
import hospital.management.backend.fhir.dto.PatientFhirResource;
import hospital.management.backend.fhir.dto.PractitionerFhirResource;
import hospital.management.backend.fhir.service.FhirService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fhir")
@RequiredArgsConstructor
public class FhirController {
    private final FhirService fhirService;

    @GetMapping("/Patient/{id}")
    public ResponseEntity<PatientFhirResource> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(fhirService.getPatient(id));
    }

    @GetMapping("/Practitioner/{id}")
    public ResponseEntity<PractitionerFhirResource> getPractitioner(@PathVariable Long id) {
        return ResponseEntity.ok(fhirService.getPractitioner(id));
    }

    @GetMapping("/Appointment/{id}")
    public ResponseEntity<AppointmentFhirResource> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(fhirService.getAppointment(id));
    }
}
