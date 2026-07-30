package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.AppointmentRequest;
import hospital.management.backend.dto.response.AppointmentResponse;
import hospital.management.backend.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getReason()
        );
    }

    public Appointment toEntity(AppointmentRequest request) {
        return Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .build();
    }
}
