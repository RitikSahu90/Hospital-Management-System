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
import java.util.Collections;
import java.util.List;
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
        assertEquals("Paracetamol", result.getName());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void shouldUpdateMedicine() {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setManufacturer("PharmaCo");
        request.setUnitPrice(BigDecimal.valueOf(15.0));
        request.setStockQuantity(200);
        request.setExpiryDate(LocalDate.now().plusMonths(6));

        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setStockQuantity(100);
        MedicineResponse response = new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.valueOf(15.0), 200, request.getExpiryDate());

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(medicine)).thenReturn(medicine);
        when(medicineMapper.toResponse(medicine)).thenReturn(response);

        MedicineResponse result = medicineService.update(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getName());
        assertEquals(BigDecimal.valueOf(15.0), result.getUnitPrice());
        assertEquals(200, result.getStockQuantity());
        verify(medicineRepository).save(medicine);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentMedicine() {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setManufacturer("PharmaCo");
        request.setUnitPrice(BigDecimal.valueOf(15.0));
        request.setStockQuantity(200);
        request.setExpiryDate(LocalDate.now().plusMonths(6));

        when(medicineRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.update(99L, request));

        assertEquals("Medicine not found", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void shouldDeleteMedicine() {
        medicineService.delete(1L);

        verify(medicineRepository).deleteById(1L);
    }

    @Test
    void shouldFindMedicineById() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Paracetamol");
        medicine.setManufacturer("PharmaCo");
        medicine.setUnitPrice(BigDecimal.valueOf(12.5));
        medicine.setStockQuantity(100);
        medicine.setExpiryDate(LocalDate.now().plusMonths(6));

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(medicineMapper.toResponse(medicine)).thenReturn(new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.valueOf(12.5), 100, medicine.getExpiryDate()));

        MedicineResponse result = medicineService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getName());
    }

    @Test
    void shouldThrowWhenFindingNonExistentMedicine() {
        when(medicineRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.findById(99L));

        assertEquals("Medicine not found", exception.getMessage());
    }

    @Test
    void shouldFindAllMedicines() {
        Medicine medicine1 = new Medicine();
        medicine1.setId(1L);
        medicine1.setName("Paracetamol");
        Medicine medicine2 = new Medicine();
        medicine2.setId(2L);
        medicine2.setName("Ibuprofen");

        when(medicineRepository.findAll()).thenReturn(List.of(medicine1, medicine2));
        when(medicineMapper.toResponse(medicine1)).thenReturn(new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.valueOf(12.5), 100, LocalDate.now().plusMonths(6)));
        when(medicineMapper.toResponse(medicine2)).thenReturn(new MedicineResponse(2L, "Ibuprofen", "PharmaCo", BigDecimal.valueOf(20), 50, LocalDate.now().plusMonths(3)));

        List<MedicineResponse> result = medicineService.findAll();

        assertEquals(2, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
        assertEquals("Ibuprofen", result.get(1).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoMedicines() {
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());

        List<MedicineResponse> result = medicineService.findAll();

        assertTrue(result.isEmpty());
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

    @Test
    void shouldThrowWhenReduceStockWithNullQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.reduceStock(1L, null));

        assertEquals("Quantity must be positive", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenReduceStockWithZeroOrNegativeQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.reduceStock(1L, 0));

        assertEquals("Quantity must be positive", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenReduceStockForNonExistentMedicine() {
        when(medicineRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.reduceStock(99L, 5));

        assertEquals("Medicine not found", exception.getMessage());
    }

    @Test
    void shouldThrowWhenIncreaseStockWithNullQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.increaseStock(1L, null));

        assertEquals("Quantity must be positive", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenIncreaseStockWithZeroOrNegativeQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.increaseStock(1L, -5));

        assertEquals("Quantity must be positive", exception.getMessage());
        verify(medicineRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenIncreaseStockForNonExistentMedicine() {
        when(medicineRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> medicineService.increaseStock(99L, 5));

        assertEquals("Medicine not found", exception.getMessage());
    }
}