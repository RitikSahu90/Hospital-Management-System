package hospital.management.backend.notification.service;

import hospital.management.backend.notification.event.AppointmentNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApplicationEventPublisher eventPublisher;

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

    private void publishEvent(String subject, String recipient, String message, String type) {
        eventPublisher.publishEvent(new AppointmentNotificationEvent(recipient, subject, message, type));
    }
}
