package hospital.management.backend.service;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Inventory;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Supplier;
import hospital.management.backend.mapper.MedicineMapper;
import hospital.management.backend.repository.InventoryRepository;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.repository.SupplierRepository;
import hospital.management.backend.service.impl.MedicineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicineServiceTest {
    @Mock private MedicineRepository medicineRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private SupplierRepository supplierRepository;
    @Mock private MedicineMapper medicineMapper;
    @InjectMocks private MedicineServiceImpl medicineService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    private MedicineRequest request() {
        MedicineRequest request = new MedicineRequest(); request.setSupplierId(2L); request.setName("Paracetamol"); request.setManufacturer("PharmaCo"); request.setUnitPrice(new BigDecimal("12.50")); request.setStockQuantity(10); request.setReorderLevel(3); request.setExpiryDate(LocalDate.now().plusMonths(6)); return request;
    }

    private Inventory inventory(Medicine medicine, int stock) { return Inventory.builder().medicine(medicine).stockQuantity(stock).reorderLevel(3).expiryDate(LocalDate.now().plusMonths(6)).build(); }

    @Test void createsMedicineAndInventory() {
        Medicine medicine = new Medicine(); medicine.setId(1L); Supplier supplier = new Supplier();
        Inventory inventory = inventory(medicine, 10);
        when(supplierRepository.findById(2L)).thenReturn(Optional.of(supplier)); when(medicineMapper.toEntity(any())).thenReturn(medicine); when(medicineRepository.save(medicine)).thenReturn(medicine); when(inventoryRepository.save(any())).thenReturn(inventory); when(medicineMapper.toResponse(medicine, inventory)).thenReturn(new MedicineResponse(1L, "Paracetamol", "PharmaCo", new BigDecimal("12.50"), 10, inventory.getExpiryDate()));
        assertEquals(1L, medicineService.create(request()).getId());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test void reducesInventoryStock() {
        Medicine medicine = new Medicine(); medicine.setId(1L); Inventory inventory = inventory(medicine, 10);
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine)); when(inventoryRepository.findByMedicine(medicine)).thenReturn(Optional.of(inventory)); when(inventoryRepository.save(inventory)).thenReturn(inventory); when(medicineMapper.toResponse(medicine, inventory)).thenReturn(new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.TEN, 5, inventory.getExpiryDate()));
        assertEquals(5, medicineService.reduceStock(1L, 5).getStockQuantity());
        assertEquals(5, inventory.getStockQuantity());
    }

    @Test void rejectsInsufficientInventory() {
        Medicine medicine = new Medicine(); medicine.setId(1L); Inventory inventory = inventory(medicine, 3);
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine)); when(inventoryRepository.findByMedicine(medicine)).thenReturn(Optional.of(inventory));
        assertThrows(IllegalArgumentException.class, () -> medicineService.reduceStock(1L, 5));
        verify(inventoryRepository, never()).save(any());
    }

    @Test void listsMedicinesWithInventory() {
        Medicine medicine = new Medicine(); medicine.setId(1L); Inventory inventory = inventory(medicine, 10);
        when(medicineRepository.findAll()).thenReturn(List.of(medicine)); when(inventoryRepository.findByMedicine(medicine)).thenReturn(Optional.of(inventory)); when(medicineMapper.toResponse(medicine, inventory)).thenReturn(new MedicineResponse(1L, "Paracetamol", "PharmaCo", BigDecimal.TEN, 10, inventory.getExpiryDate()));
        assertEquals(1, medicineService.findAll().size());
    }
}
