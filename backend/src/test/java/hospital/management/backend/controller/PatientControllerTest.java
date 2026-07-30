package hospital.management.backend.controller;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import hospital.management.backend.config.TestSecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    @WithMockUser
    void shouldGetPatients() throws Exception {
        when(patientService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldCreatePatient() throws Exception {
        Patient patient = Patient.builder().id(1L).firstName("Asha").lastName("Patel").email("asha@example.com").phone("9876543210").diagnosis("Hypertension").build();
        when(patientService.create(org.mockito.ArgumentMatchers.any(Patient.class))).thenReturn(new hospital.management.backend.dto.response.PatientResponse(1L, "Asha", "Patel", "asha@example.com", "9876543210", "Hypertension"));

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Asha\",\"lastName\":\"Patel\",\"email\":\"asha@example.com\",\"phone\":\"9876543210\",\"diagnosis\":\"Hypertension\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
