package hospital.management.backend.fhir;

import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.fhir.mapper.FhirMapper;
import hospital.management.backend.fhir.service.FhirService;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FhirServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    private FhirService fhirService;

    @BeforeEach
    void setUp() {
        fhirService = new FhirService(patientRepository, doctorRepository, appointmentRepository, new FhirMapper());
    }

    @Test
    void shouldMapPatientToFhirResource() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Asha")
                .lastName("Patel")
                .phone("9876543210")
                .email("asha@example.com")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        var resource = fhirService.getPatient(1L);

        assertThat(resource.getId()).isEqualTo(1L);
        assertThat(resource.getName()).isEqualTo("Asha Patel");
        assertThat(resource.getGender()).isEqualTo("unknown");
        assertThat(resource.getTelecom()).contains("9876543210", "asha@example.com");
        assertThat(resource.getAddress()).isEmpty();
    }

    @Test
    void shouldMapPatientWithNullContactInfo() {
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("Asha")
                .lastName("Patel")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        var resource = fhirService.getPatient(1L);

        assertThat(resource.getId()).isEqualTo(1L);
        assertThat(resource.getName()).isEqualTo("Asha Patel");
        assertThat(resource.getTelecom()).isEmpty();
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fhirService.getPatient(99L));

        assertThat(exception.getMessage()).isEqualTo("Patient not found");
    }

    @Test
    void shouldMapDoctorToFhirPractitionerResource() {
        Doctor doctor = Doctor.builder()
                .id(2L)
                .firstName("Ravi")
                .lastName("Sharma")
                .specialization("Cardiology")
                .licenseNumber("LIC-100")
                .consultationFee(500.0)
                .build();

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        var resource = fhirService.getPractitioner(2L);

        assertThat(resource.getId()).isEqualTo(2L);
        assertThat(resource.getName()).isEqualTo("Ravi Sharma");
        assertThat(resource.getSpecialization()).isEqualTo("Cardiology");
        assertThat(resource.getQualification()).isEqualTo("N/A");
    }

    @Test
    void shouldThrowWhenPractitionerNotFound() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fhirService.getPractitioner(99L));

        assertThat(exception.getMessage()).isEqualTo("Practitioner not found");
    }

    @Test
    void shouldMapAppointmentToFhirResource() {
        Patient patient = Patient.builder().id(1L).firstName("Asha").lastName("Patel").build();
        Doctor doctor = Doctor.builder().id(2L).firstName("Ravi").lastName("Sharma").build();
        Appointment appointment = Appointment.builder()
                .id(10L)
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDate.of(2026, 7, 30))
                .appointmentTime(LocalTime.of(10, 30))
                .status(AppointmentStatus.SCHEDULED)
                .reason("Follow-up")
                .build();

        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));

        var resource = fhirService.getAppointment(10L);

        assertThat(resource.getId()).isEqualTo(10L);
        assertThat(resource.getPatient().getDisplay()).isEqualTo("Asha Patel");
        assertThat(resource.getPatient().getReference()).isEqualTo("Patient/1");
        assertThat(resource.getDoctor().getDisplay()).isEqualTo("Ravi Sharma");
        assertThat(resource.getDoctor().getReference()).isEqualTo("Practitioner/2");
        assertThat(resource.getStatus()).isEqualTo("SCHEDULED");
        assertThat(resource.getStart()).isNotNull();
        assertThat(resource.getEnd()).isNotNull();
    }

    @Test
    void shouldThrowWhenAppointmentNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fhirService.getAppointment(99L));

        assertThat(exception.getMessage()).isEqualTo("Appointment not found");
    }
}