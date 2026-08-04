package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.LabOrderRequest;
import hospital.management.backend.dto.response.LabTestResponse;
import hospital.management.backend.dto.response.LabOrderResponse;
import hospital.management.backend.entity.LabTest;
import hospital.management.backend.entity.LabOrder;
import hospital.management.backend.enums.LabOrderStatus;
import org.springframework.stereotype.Component;

@Component
public class LabMapper {

    public LabTestResponse toResponse(LabTest test) {
        if (test == null) return null;
        return new LabTestResponse(
                test.getId(),
                test.getName(),
                test.getCategory(),
                test.getDescription(),
                test.getNormalRange(),
                test.getUnit(),
                test.getPrice(),
                test.getIsActive()
        );
    }

    public LabOrderResponse toResponse(LabOrder order) {
        if (order == null) return null;
        return new LabOrderResponse(
                order.getId(),
                order.getPatient().getId(),
                order.getPatient().getFirstName() + " " + order.getPatient().getLastName(),
                order.getTest().getId(),
                order.getTest().getName(),
                order.getTest().getCategory().name(),
                order.getOrderedBy().getId(),
                "Dr. " + order.getOrderedBy().getFirstName() + " " + order.getOrderedBy().getLastName(),
                order.getAppointment() != null ? order.getAppointment().getId() : null,
                order.getOrderNumber(),
                order.getStatus(),
                order.getResultValue(),
                order.getResultUrl(),
                order.getRemarks(),
                order.getOrderedAt(),
                order.getCompletedAt(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public LabOrder toEntity(LabOrderRequest request) {
        if (request == null) return null;
        return LabOrder.builder()
                .status(request.getStatus() != null ? LabOrderStatus.valueOf(request.getStatus().toUpperCase()) : LabOrderStatus.PENDING)
                .resultValue(request.getResultValue())
                .resultUrl(request.getResultUrl())
                .remarks(request.getRemarks())
                .build();
    }
}
