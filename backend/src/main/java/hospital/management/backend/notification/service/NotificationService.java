package hospital.management.backend.notification.service;

import hospital.management.backend.notification.event.AppointmentNotificationEvent;
import hospital.management.backend.repository.NotificationRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.dto.response.NotificationResponse;
import hospital.management.backend.entity.Notification;
import hospital.management.backend.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationRepository notificationRepository;
    private final PatientRepository patientRepository;

    public void notifyAppointmentCreated(String recipient, String message) {
        publishEvent("Appointment Created", recipient, message, "APPOINTMENT_CREATED");
    }

    public void notifyAppointmentCancelled(String recipient, String message) {
        publishEvent("Appointment Cancelled", recipient, message, "APPOINTMENT_CANCELLED");
    }

    public void notifyAppointmentReminder(String recipient, String message) {
        publishEvent("Appointment Reminder", recipient, message, "APPOINTMENT_REMINDER");
    }

    public void notifyBillGenerated(String recipient, String message) {
        publishEvent("Bill Generated", recipient, message, "BILL_GENERATED");
    }

    public void createNotification(Patient patient, String title, String message) {
        Notification notification = Notification.builder()
                .patient(patient)
                .title(title)
                .message(message)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotificationsForPatient(String username) {
        return patientRepository.findByUserUsername(username)
                .map(patient -> notificationRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId()).stream()
                        .map(n -> new NotificationResponse(n.getId(), n.getTitle(), n.getMessage(), n.getCreatedAt(), n.isRead()))
                        .toList())
                .orElse(Collections.emptyList());
    }

    private void publishEvent(String subject, String recipient, String message, String type) {
        eventPublisher.publishEvent(new AppointmentNotificationEvent(recipient, subject, message, type));
    }
}

