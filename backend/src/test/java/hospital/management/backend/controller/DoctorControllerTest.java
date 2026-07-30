package hospital.management.backend.controller;

import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.service.DoctorService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    @WithMockUser
    void shouldGetDoctors() throws Exception {
        when(doctorService.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldCreateDoctor() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setFirstName("Ravi");
        request.setLastName("Sharma");
        request.setLicenseNumber("LIC-9");
        request.setSpecialization("Cardiology");

        when(doctorService.create(org.mockito.ArgumentMatchers.any(DoctorRequest.class)))
                .thenReturn(new DoctorResponse(1L, "Ravi", "Sharma", "LIC-9", "Cardiology", null, null));

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ravi\",\"lastName\":\"Sharma\",\"licenseNumber\":\"LIC-9\",\"specialization\":\"Cardiology\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
