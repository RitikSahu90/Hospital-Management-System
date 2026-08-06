package hospital.management.backend.service;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.enums.Gender;
import hospital.management.backend.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock private PatientRepository patientRepository;
    @Mock private hospital.management.backend.repository.DoctorRepository doctorRepository;
    @Mock private hospital.management.backend.repository.AppointmentRepository appointmentRepository;
    @InjectMocks private PatientService patientService;

    private Patient patient(Long id) {
        return Patient.builder().id(id).patientNumber("P-" + id).firstName("Asha").lastName("Patel")
                .dateOfBirth(LocalDate.of(1990, 1, 1)).gender(Gender.FEMALE).phone("9876543210")
                .email("asha" + id + "@example.com").address("Address").bloodGroup("A+").build();
    }

    @Test void mapsSchemaBackedPatientFields() {
        when(patientRepository.findAll()).thenReturn(List.of(patient(1L)));
        var result = patientService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatientNumber()).isEqualTo("P-1");
        assertThat(result.get(0).getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test void createsPatient() {
        Patient input = patient(null); Patient saved = patient(2L);
        when(patientRepository.save(input)).thenReturn(saved);
        assertThat(patientService.create(input).getId()).isEqualTo(2L);
    }

    @Test void findsPatientByLinkedUsername() {
        when(patientRepository.findByUserUsername("asha")).thenReturn(Optional.of(patient(1L)));
        assertThat(patientService.findForUsername("asha").getId()).isEqualTo(1L);
    }
}
