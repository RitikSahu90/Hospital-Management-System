package hospital.management.backend.repository;

import hospital.management.backend.entity.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, Long> {
    Optional<InsuranceProvider> findByProviderCode(String providerCode);
    boolean existsByProviderCode(String providerCode);
}
