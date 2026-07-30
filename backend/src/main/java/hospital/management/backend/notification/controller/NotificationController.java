package hospital.management.backend.notification.controller;

import hospital.management.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
