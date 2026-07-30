package hospital.management.backend.notification.service;

public interface NotificationChannel {
    void send(String recipient, String subject, String message);
}
