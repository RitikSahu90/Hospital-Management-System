package hospital.management.backend.controller;

import hospital.management.backend.dto.request.AppointmentRequest;
import hospital.management.backend.dto.request.AppointmentStatusUpdateRequest;
import hospital.management.backend.dto.response.AppointmentResponse;
import hospital.management.backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.created(URI.create("/api/appointments/" + response.getId())).body(response);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody AppointmentStatusUpdateRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(org.springframework.security.core.Authentication authentication) {
        if (authentication != null) {
            boolean isDoctor = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"));
            if (isDoctor) {
                return ResponseEntity.ok(appointmentService.findAllForDoctor(authentication.getName()));
            }
            boolean isPatient = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PATIENT"));
            if (isPatient) {
                return ResponseEntity.ok(appointmentService.findAllForPatient(authentication.getName()));
            }
        }
        return ResponseEntity.ok(appointmentService.findAll());
    }
}
