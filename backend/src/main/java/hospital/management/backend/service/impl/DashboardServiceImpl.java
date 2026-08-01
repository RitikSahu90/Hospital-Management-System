package hospital.management.backend.service.impl;

import hospital.management.backend.dto.response.DashboardResponse;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillingRepository billingRepository;

    public DashboardResponse getSummary() {
        var appointments = appointmentRepository.findAll();
        Map<String, Long> statuses = Arrays.stream(AppointmentStatus.values()).collect(Collectors.toMap(Enum::name, status -> appointments.stream().filter(appointment -> appointment.getStatus() == status).count()));
        BigDecimal revenue = billingRepository.findAll().stream().map(bill -> bill.getTotalAmount() == null ? BigDecimal.ZERO : bill.getTotalAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DashboardResponse(patientRepository.count(), doctorRepository.count(), appointments.size(), revenue, statuses);
    }
}