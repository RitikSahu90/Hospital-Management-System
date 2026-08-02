package hospital.management.backend.controller;

import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Inventory;
import hospital.management.backend.entity.Supplier;
import hospital.management.backend.repository.InventoryRepository;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

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
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private InventoryRepository inventoryRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateAndRetrieveMedicine() throws Exception {
        Supplier supplier = new Supplier(); supplier.setName("PharmaCo Supplier"); supplier.setPhone("9999999999"); supplier = supplierRepository.save(supplier);
        String json = "{\"supplierId\":" + supplier.getId() + ",\"name\":\"Paracetamol\",\"manufacturer\":\"PharmaCo\",\"unitPrice\":15.5,\"stockQuantity\":50,\"reorderLevel\":5,\"expiryDate\":\"2030-01-01\"}";

        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        Medicine saved = medicineRepository.findByName("Paracetamol").orElseThrow();
        assertThat(saved.getManufacturer()).isEqualTo("PharmaCo");
        Inventory inventory = inventoryRepository.findByMedicine(saved).orElseThrow();
        assertThat(inventory.getStockQuantity()).isEqualTo(50);
    }
}
