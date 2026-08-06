package hospital.management.backend.notification.controller;

import hospital.management.backend.dto.response.NotificationResponse;
import hospital.management.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/appointment-created")
    public ResponseEntity<String> appointmentCreated(@RequestBody Map<String, String> payload) {
        notificationService.notifyAppointmentCreated(payload.get("recipient"), payload.get("message"));
        return ResponseEntity.ok("Notification requested");
    }

    @PostMapping("/appointment-cancelled")
    public ResponseEntity<String> appointmentCancelled(@RequestBody Map<String, String> payload) {
        notificationService.notifyAppointmentCancelled(payload.get("recipient"), payload.get("message"));
        return ResponseEntity.ok("Notification requested");
    }

    @PostMapping("/appointment-reminder")
    public ResponseEntity<String> appointmentReminder(@RequestBody Map<String, String> payload) {
        notificationService.notifyAppointmentReminder(payload.get("recipient"), payload.get("message"));
        return ResponseEntity.ok("Notification requested");
    }

    @PostMapping("/bill-generated")
    public ResponseEntity<String> billGenerated(@RequestBody Map<String, String> payload) {
        notificationService.notifyBillGenerated(payload.get("recipient"), payload.get("message"));
        return ResponseEntity.ok("Notification requested");
    }

    @GetMapping("/patient")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getPatientNotifications(Authentication authentication) {
        return ResponseEntity.ok(notificationService.getNotificationsForPatient(authentication.getName()));
    }
}
