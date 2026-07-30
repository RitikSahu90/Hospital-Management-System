package hospital.management.backend.notification;

import hospital.management.backend.notification.event.AppointmentNotificationEvent;
import hospital.management.backend.notification.service.EmailNotificationService;
import hospital.management.backend.notification.service.NotificationChannel;
import hospital.management.backend.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Test
    void shouldPublishAppointmentCreatedEvent() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        NotificationService service = new NotificationService(publisher);

        service.notifyAppointmentCreated("patient@example.com", "Your appointment is confirmed");

        verify(publisher).publishEvent(any(AppointmentNotificationEvent.class));
    }

    @Test
    void shouldSendNotificationThroughChannel() {
        NotificationChannel channel = mock(NotificationChannel.class);
        EmailNotificationService emailService = new EmailNotificationService(channel);

        AppointmentNotificationEvent event = new AppointmentNotificationEvent(
                "doctor@example.com",
                "Reminder",
                "Please review the appointment",
                "APPOINTMENT_REMINDER"
        );

        emailService.handleAppointmentNotification(event);

        verify(channel).send("doctor@example.com", "Reminder", "Please review the appointment");
    }
}
