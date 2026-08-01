package hospital.management.backend.repository;

import hospital.management.backend.entity.Inventory;
import hospital.management.backend.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByMedicine(Medicine medicine);
}