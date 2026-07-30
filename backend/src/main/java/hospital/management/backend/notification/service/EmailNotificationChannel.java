package hospital.management.backend.notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String subject, String message) {
        // Mock implementation for now; replace later with real provider.
        System.out.println("[MOCK EMAIL] To=" + recipient + " | Subject=" + subject + " | Message=" + message);
    }
}
