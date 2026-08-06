package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.InsuranceClaimRequest;
import hospital.management.backend.dto.response.InsuranceProviderResponse;
import hospital.management.backend.dto.response.InsuranceClaimResponse;
import hospital.management.backend.entity.InsuranceProvider;
import hospital.management.backend.entity.InsuranceClaim;
import hospital.management.backend.enums.InsuranceClaimStatus;
import org.springframework.stereotype.Component;

@Component
public class InsuranceMapper {

    public InsuranceProviderResponse toResponse(InsuranceProvider provider) {
        if (provider == null) return null;
        return new InsuranceProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getProviderCode(),
                provider.getContactPhone(),
                provider.getContactEmail(),
                provider.getWebsite(),
                provider.getIsActive(),
                provider.getCreatedAt(),
                provider.getUpdatedAt()
        );
    }

    public InsuranceClaimResponse toResponse(InsuranceClaim claim) {
        if (claim == null) return null;
        return new InsuranceClaimResponse(
                claim.getId(),
                claim.getBill().getId(),
                claim.getPatient().getId(),
                claim.getPatient().getFirstName() + " " + claim.getPatient().getLastName(),
                claim.getProvider().getId(),
                claim.getProvider().getName(),
                claim.getClaimNumber(),
                claim.getPolicyNumber(),
                claim.getAmountClaimed(),
                claim.getAmountApproved(),
                claim.getAmountSettled(),
                claim.getStatus(),
                claim.getRejectionReason(),
                claim.getSubmittedAt(),
                claim.getApprovedAt(),
                claim.getSettledAt(),
                claim.getNotes(),
                claim.getCreatedAt(),
                claim.getUpdatedAt()
        );
    }

    public InsuranceClaim toEntity(InsuranceClaimRequest request) {
        if (request == null) return null;
        return InsuranceClaim.builder()
                .policyNumber(request.getPolicyNumber())
                .amountClaimed(request.getAmountClaimed())
                .amountApproved(request.getAmountApproved())
                .amountSettled(request.getAmountSettled())
                .status(request.getStatus() != null ? InsuranceClaimStatus.valueOf(request.getStatus().toUpperCase()) : InsuranceClaimStatus.DRAFT)
                .rejectionReason(request.getRejectionReason())
                .notes(request.getNotes())
                .build();
    }
}
