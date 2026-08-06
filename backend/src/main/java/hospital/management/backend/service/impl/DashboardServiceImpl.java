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

    @Override
    public DashboardResponse getSummary(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"))) {
            String username = authentication.getName();
            hospital.management.backend.entity.Doctor doctor = doctorRepository.findByUserUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));
            
            var appointments = appointmentRepository.findAll().stream()
                    .filter(app -> app.getDoctor() != null && app.getDoctor().getId().equals(doctor.getId()))
                    .toList();
            
            Map<String, Long> statuses = Arrays.stream(AppointmentStatus.values())
                    .collect(Collectors.toMap(Enum::name, status -> appointments.stream().filter(appointment -> appointment.getStatus() == status).count()));
            
            BigDecimal revenue = billingRepository.findAll().stream()
                    .filter(bill -> bill.getAppointment() != null && bill.getAppointment().getDoctor() != null && bill.getAppointment().getDoctor().getId().equals(doctor.getId()))
                    .map(bill -> bill.getTotalAmount() == null ? BigDecimal.ZERO : bill.getTotalAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            long patientCount = appointments.stream()
                    .map(app -> app.getPatient())
                    .filter(java.util.Objects::nonNull)
                    .map(p -> p.getId())
                    .distinct()
                    .count();
            
            return new DashboardResponse(patientCount, 1L, appointments.size(), revenue, statuses);
        }

        var appointments = appointmentRepository.findAll();
        Map<String, Long> statuses = Arrays.stream(AppointmentStatus.values()).collect(Collectors.toMap(Enum::name, status -> appointments.stream().filter(appointment -> appointment.getStatus() == status).count()));
        BigDecimal revenue = billingRepository.findAll().stream().map(bill -> bill.getTotalAmount() == null ? BigDecimal.ZERO : bill.getTotalAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DashboardResponse(patientRepository.count(), doctorRepository.count(), appointments.size(), revenue, statuses);
    }
}