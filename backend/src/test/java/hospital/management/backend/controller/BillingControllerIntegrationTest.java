package hospital.management.backend.controller;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BillingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private hospital.management.backend.repository.AppointmentRepository appointmentRepository;

    @Autowired
    private hospital.management.backend.repository.PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        billingRepository.deleteAll();
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateBillingAndPersist() throws Exception {
        Patient patient = new Patient();
        patient.setPatientNumber("P-BILL-1");
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        patient.setGender(hospital.management.backend.enums.Gender.MALE);
        patient.setEmail("john@example.com");
        patient.setPhone("9999999999");
        patient = patientRepository.save(patient);
        String json = "{\"patientId\":" + patient.getId() + ",\"consultationFee\":100.0,\"medicineCharges\":0.0,\"otherCharges\":0.0}";

        mockMvc.perform(post("/api/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        assertThat(billingRepository.findAll()).hasSize(1);
    }
}
