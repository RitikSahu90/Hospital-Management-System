package hospital.management.backend.service;

import hospital.management.backend.dto.request.AppointmentRequest;
import hospital.management.backend.dto.request.AppointmentStatusUpdateRequest;
import hospital.management.backend.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
    AppointmentResponse cancelAppointment(Long id);
    AppointmentResponse updateStatus(Long id, AppointmentStatusUpdateRequest request);
    AppointmentResponse findById(Long id);
    List<AppointmentResponse> findAll();
}
