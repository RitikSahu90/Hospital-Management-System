package hospital.management.backend.notification.service;

import hospital.management.backend.notification.event.AppointmentNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final NotificationChannel notificationChannel;

    @EventListener
    public void handleAppointmentNotification(AppointmentNotificationEvent event) {
        notificationChannel.send(event.getRecipient(), event.getSubject(), event.getMessage());
    }
}
