package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public Doctor toEntity(DoctorRequest req) {
        return Doctor.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .licenseNumber(req.getLicenseNumber())
                .specialization(req.getSpecialization())
                .phone(req.getPhone())
                .consultationFee(req.getConsultationFee())
                .doctorCode(req.getDoctorCode())
                .yearsExperience(req.getYearsExperience())
                .status(req.getStatus())
                .build();
    }

    public DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(
                d.getId(),
                d.getFirstName(),
                d.getLastName(),
                d.getLicenseNumber(),
                d.getSpecialization(),
                d.getPhone(),
                d.getConsultationFee()
        );
    }
}
