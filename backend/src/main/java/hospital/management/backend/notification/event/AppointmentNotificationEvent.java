package hospital.management.backend.notification.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentNotificationEvent {
    private final String recipient;
    private final String subject;
    private final String message;
    private final String type;
}
