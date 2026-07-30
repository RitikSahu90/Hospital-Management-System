package hospital.management.backend.fhir.mapper;

import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.fhir.dto.AppointmentFhirResource;
import hospital.management.backend.fhir.dto.PatientFhirResource;
import hospital.management.backend.fhir.dto.PractitionerFhirResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FhirMapper {

    public PatientFhirResource toPatientResource(Patient patient) {
        return PatientFhirResource.builder()
                .id(patient.getId())
                .name(patient.getFirstName() + " " + patient.getLastName())
                .gender("unknown")
                .birthDate(null)
                .telecom(List.of(
                        patient.getPhone() == null ? "" : patient.getPhone(),
                        patient.getEmail() == null ? "" : patient.getEmail()
                ).stream().filter(value -> !value.isBlank()).toList())
                .address(List.of())
                .build();
    }

    public PractitionerFhirResource toPractitionerResource(Doctor doctor) {
        return PractitionerFhirResource.builder()
                .id(doctor.getId())
                .name(doctor.getFirstName() + " " + doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .qualification("N/A")
                .build();
    }

    public AppointmentFhirResource toAppointmentResource(Appointment appointment) {
        return AppointmentFhirResource.builder()
                .id(appointment.getId())
                .patient(AppointmentFhirResource.Reference.builder()
                        .reference("Patient/" + appointment.getPatient().getId())
                        .display(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName())
                        .build())
                .doctor(AppointmentFhirResource.Reference.builder()
                        .reference("Practitioner/" + appointment.getDoctor().getId())
                        .display(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName())
                        .build())
                .status(appointment.getStatus().name())
                .start(appointment.getAppointmentDate().atTime(appointment.getAppointmentTime()).toString())
                .end(appointment.getAppointmentDate().atTime(appointment.getAppointmentTime().plusHours(1)).toString())
                .build();
    }
}
