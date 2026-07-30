package hospital.management.backend.controller;

import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.repository.MedicineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class MedicineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateAndRetrieveMedicine() throws Exception {
        String json = "{\"name\":\"Paracetamol\",\"manufacturer\":\"PharmaCo\",\"unitPrice\":15.5,\"stockQuantity\":50,\"expiryDate\":\"" + LocalDate.now().plusMonths(6) + "\"}";

        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        Medicine saved = medicineRepository.findByName("Paracetamol").orElseThrow();
        assertThat(saved.getManufacturer()).isEqualTo("PharmaCo");
        assertThat(saved.getStockQuantity()).isEqualTo(50);
    }
}
