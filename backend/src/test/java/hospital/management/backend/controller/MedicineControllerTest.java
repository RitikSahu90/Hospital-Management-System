package hospital.management.backend.controller;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.security.JwtAuthenticationFilter;
import hospital.management.backend.security.jwt.JwtUtil;
import hospital.management.backend.service.MedicineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicineController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineService medicineService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateMedicine() throws Exception {
        MedicineResponse response = new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.valueOf(12.5), 100, LocalDate.now().plusMonths(6));

        Mockito.when(medicineService.create(Mockito.any())).thenReturn(response);

        String json = "{\"supplierId\":1,\"name\":\"Paracetamol\",\"manufacturer\":\"PharmaCo\",\"unitPrice\":12.5,\"stockQuantity\":100,\"reorderLevel\":10,\"expiryDate\":\"2030-01-01\"}";

        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/medicines/1"));
    }
}
