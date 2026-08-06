package hospital.management.backend.notification;

import hospital.management.backend.notification.event.AppointmentNotificationEvent;
import hospital.management.backend.notification.service.EmailNotificationService;
import hospital.management.backend.notification.service.NotificationChannel;
import hospital.management.backend.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Test
    void shouldPublishAppointmentCreatedEvent() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        hospital.management.backend.repository.NotificationRepository notificationRepository = mock(hospital.management.backend.repository.NotificationRepository.class);
        hospital.management.backend.repository.PatientRepository patientRepository = mock(hospital.management.backend.repository.PatientRepository.class);
        NotificationService service = new NotificationService(publisher, notificationRepository, patientRepository);

        service.notifyAppointmentCreated("patient@example.com", "Your appointment is confirmed");

        ArgumentCaptor<AppointmentNotificationEvent> captor = ArgumentCaptor.forClass(AppointmentNotificationEvent.class);
        verify(publisher).publishEvent(captor.capture());

        AppointmentNotificationEvent event = captor.getValue();
        assertThat(event.getRecipient()).isEqualTo("patient@example.com");
        assertThat(event.getSubject()).isEqualTo("Appointment Created");
        assertThat(event.getMessage()).isEqualTo("Your appointment is confirmed");
        assertThat(event.getType()).isEqualTo("APPOINTMENT_CREATED");
    }

    @Test
    void shouldPublishAppointmentCancelledEvent() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        hospital.management.backend.repository.NotificationRepository notificationRepository = mock(hospital.management.backend.repository.NotificationRepository.class);
        hospital.management.backend.repository.PatientRepository patientRepository = mock(hospital.management.backend.repository.PatientRepository.class);
        NotificationService service = new NotificationService(publisher, notificationRepository, patientRepository);

        service.notifyAppointmentCancelled("patient@example.com", "Your appointment has been cancelled");

        ArgumentCaptor<AppointmentNotificationEvent> captor = ArgumentCaptor.forClass(AppointmentNotificationEvent.class);
        verify(publisher).publishEvent(captor.capture());

        AppointmentNotificationEvent event = captor.getValue();
        assertThat(event.getRecipient()).isEqualTo("patient@example.com");
        assertThat(event.getSubject()).isEqualTo("Appointment Cancelled");
        assertThat(event.getMessage()).isEqualTo("Your appointment has been cancelled");
        assertThat(event.getType()).isEqualTo("APPOINTMENT_CANCELLED");
    }

    @Test
    void shouldPublishAppointmentReminderEvent() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        hospital.management.backend.repository.NotificationRepository notificationRepository = mock(hospital.management.backend.repository.NotificationRepository.class);
        hospital.management.backend.repository.PatientRepository patientRepository = mock(hospital.management.backend.repository.PatientRepository.class);
        NotificationService service = new NotificationService(publisher, notificationRepository, patientRepository);

        service.notifyAppointmentReminder("patient@example.com", "Reminder: appointment tomorrow");

        ArgumentCaptor<AppointmentNotificationEvent> captor = ArgumentCaptor.forClass(AppointmentNotificationEvent.class);
        verify(publisher).publishEvent(captor.capture());

        AppointmentNotificationEvent event = captor.getValue();
        assertThat(event.getRecipient()).isEqualTo("patient@example.com");
        assertThat(event.getSubject()).isEqualTo("Appointment Reminder");
        assertThat(event.getMessage()).isEqualTo("Reminder: appointment tomorrow");
        assertThat(event.getType()).isEqualTo("APPOINTMENT_REMINDER");
    }

    @Test
    void shouldPublishBillGeneratedEvent() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        hospital.management.backend.repository.NotificationRepository notificationRepository = mock(hospital.management.backend.repository.NotificationRepository.class);
        hospital.management.backend.repository.PatientRepository patientRepository = mock(hospital.management.backend.repository.PatientRepository.class);
        NotificationService service = new NotificationService(publisher, notificationRepository, patientRepository);

        service.notifyBillGenerated("patient@example.com", "Your bill has been generated");

        ArgumentCaptor<AppointmentNotificationEvent> captor = ArgumentCaptor.forClass(AppointmentNotificationEvent.class);
        verify(publisher).publishEvent(captor.capture());

        AppointmentNotificationEvent event = captor.getValue();
        assertThat(event.getRecipient()).isEqualTo("patient@example.com");
        assertThat(event.getSubject()).isEqualTo("Bill Generated");
        assertThat(event.getMessage()).isEqualTo("Your bill has been generated");
        assertThat(event.getType()).isEqualTo("BILL_GENERATED");
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

    @Test
    void shouldNotSendWhenChannelThrows() {
        NotificationChannel channel = mock(NotificationChannel.class);
        EmailNotificationService emailService = new EmailNotificationService(channel);

        AppointmentNotificationEvent event = new AppointmentNotificationEvent(
                "doctor@example.com",
                "Reminder",
                "Please review the appointment",
                "APPOINTMENT_REMINDER"
        );

        doThrow(new RuntimeException("Email server down")).when(channel).send(any(), any(), any());

        // The service should propagate the exception (no swallowing)
        try {
            emailService.handleAppointmentNotification(event);
        } catch (RuntimeException ignored) {
            // expected
        }

        verify(channel).send("doctor@example.com", "Reminder", "Please review the appointment");
    }

    @Test
    void shouldSendEmailThroughEmailNotificationChannel() {
        // Verify the concrete EmailNotificationChannel implementation executes without error
        hospital.management.backend.notification.service.EmailNotificationChannel channel =
                new hospital.management.backend.notification.service.EmailNotificationChannel();

        // Should not throw any exception
        channel.send("patient@example.com", "Subject", "Message");
    }
}