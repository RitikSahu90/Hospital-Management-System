package hospital.management.backend.service;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.mapper.MedicineMapper;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.service.impl.MedicineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicineServiceTest {
    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineMapper medicineMapper;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateMedicine() {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setManufacturer("PharmaCo");
        request.setUnitPrice(BigDecimal.valueOf(12.5));
        request.setStockQuantity(100);
        request.setExpiryDate(LocalDate.now().plusMonths(6));

        Medicine medicine = new Medicine();
        Medicine saved = new Medicine();
        saved.setId(1L);
        MedicineResponse response = new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.valueOf(12.5), 100, request.getExpiryDate());

        when(medicineMapper.toEntity(request)).thenReturn(medicine);
        when(medicineRepository.save(medicine)).thenReturn(saved);
        when(medicineMapper.toResponse(saved)).thenReturn(response);

        MedicineResponse result = medicineService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void shouldReduceStock() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setStockQuantity(10);
        medicine.setName("Ibuprofen");
        medicine.setManufacturer("PharmaCo");
        medicine.setUnitPrice(BigDecimal.valueOf(20));
        medicine.setExpiryDate(LocalDate.now().plusMonths(3));

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any())).thenReturn(medicine);
        when(medicineMapper.toResponse(any())).thenReturn(new MedicineResponse(1L, "Ibuprofen", "PharmaCo", BigDecimal.valueOf(20), 5, medicine.getExpiryDate()));

        MedicineResponse result = medicineService.reduceStock(1L, 5);

        assertNotNull(result);
        assertEquals(5, result.getStockQuantity());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void shouldIncreaseStock() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setStockQuantity(10);
        medicine.setName("Amoxicillin");
        medicine.setManufacturer("PharmaCo");
        medicine.setUnitPrice(BigDecimal.valueOf(18));
        medicine.setExpiryDate(LocalDate.now().plusMonths(4));

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any())).thenReturn(medicine);
        when(medicineMapper.toResponse(any())).thenReturn(new MedicineResponse(1L, "Amoxicillin", "PharmaCo", BigDecimal.valueOf(18), 15, medicine.getExpiryDate()));

        MedicineResponse result = medicineService.increaseStock(1L, 5);

        assertNotNull(result);
        assertEquals(15, result.getStockQuantity());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void shouldThrowWhenReduceStockHasInsufficientQuantity() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setStockQuantity(3);
        medicine.setName("Aspirin");
        medicine.setManufacturer("PharmaCo");
        medicine.setUnitPrice(BigDecimal.valueOf(10));
        medicine.setExpiryDate(LocalDate.now().plusMonths(2));

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> medicineService.reduceStock(1L, 5));

        assertEquals("Insufficient stock", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }
}
