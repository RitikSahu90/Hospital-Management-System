package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceProviderResponse {
    private Long id;
    private String name;
    private String providerCode;
    private String contactPhone;
    private String contactEmail;
    private String website;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
