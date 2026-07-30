package hospital.management.backend.service;

import hospital.management.backend.dto.response.PatientResponse;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldFindAllPatients() {
        Patient patient = Patient.builder().id(1L).firstName("Asha").lastName("Patel").email("asha@example.com").phone("9876543210").diagnosis("Hypertension").build();
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<PatientResponse> result = patientService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Asha");
        assertThat(result.get(0).getLastName()).isEqualTo("Patel");
        assertThat(result.get(0).getEmail()).isEqualTo("asha@example.com");
        assertThat(result.get(0).getPhone()).isEqualTo("9876543210");
        assertThat(result.get(0).getDiagnosis()).isEqualTo("Hypertension");
    }

    @Test
    void shouldReturnEmptyListWhenNoPatients() {
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        List<PatientResponse> result = patientService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnMultiplePatients() {
        Patient patient1 = Patient.builder().id(1L).firstName("Asha").lastName("Patel").email("asha@example.com").phone("9876543210").diagnosis("Hypertension").build();
        Patient patient2 = Patient.builder().id(2L).firstName("Ravi").lastName("Sharma").email("ravi@example.com").phone("9123456780").diagnosis("Diabetes").build();
        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2));

        List<PatientResponse> result = patientService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldCreatePatient() {
        Patient input = Patient.builder().firstName("Ravi").lastName("Sharma").email("ravi@example.com").phone("9123456780").diagnosis("Diabetes").build();
        Patient saved = Patient.builder().id(2L).firstName("Ravi").lastName("Sharma").email("ravi@example.com").phone("9123456780").diagnosis("Diabetes").build();
        when(patientRepository.save(input)).thenReturn(saved);

        PatientResponse result = patientService.create(input);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("ravi@example.com");
        assertThat(result.getFirstName()).isEqualTo("Ravi");
        assertThat(result.getLastName()).isEqualTo("Sharma");
        assertThat(result.getPhone()).isEqualTo("9123456780");
        assertThat(result.getDiagnosis()).isEqualTo("Diabetes");
        verify(patientRepository).save(input);
    }

    @Test
    void shouldCreatePatientWithNullOptionalFields() {
        Patient input = Patient.builder().firstName("Asha").lastName("Patel").email("asha@example.com").build();
        Patient saved = Patient.builder().id(3L).firstName("Asha").lastName("Patel").email("asha@example.com").phone(null).diagnosis(null).build();
        when(patientRepository.save(input)).thenReturn(saved);

        PatientResponse result = patientService.create(input);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getPhone()).isNull();
        assertThat(result.getDiagnosis()).isNull();
        verify(patientRepository).save(input);
    }
}