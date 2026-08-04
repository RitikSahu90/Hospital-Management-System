package hospital.management.backend.fhir.service;

import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.fhir.dto.AppointmentFhirResource;
import hospital.management.backend.fhir.dto.PatientFhirResource;
import hospital.management.backend.fhir.dto.PractitionerFhirResource;
import hospital.management.backend.fhir.mapper.FhirMapper;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FhirService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final FhirMapper fhirMapper;

    public PatientFhirResource getPatient(Long id) {
        Patient patient = patientRepository.findById(Objects.requireNonNull(id, "Patient id must not be null"))
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return fhirMapper.toPatientResource(patient);
    }

    public PractitionerFhirResource getPractitioner(Long id) {
        Doctor doctor = doctorRepository.findById(Objects.requireNonNull(id, "Practitioner id must not be null"))
                .orElseThrow(() -> new IllegalArgumentException("Practitioner not found"));
        return fhirMapper.toPractitionerResource(doctor);
    }

    public AppointmentFhirResource getAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(Objects.requireNonNull(id, "Appointment id must not be null"))
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return fhirMapper.toAppointmentResource(appointment);
    }
}
