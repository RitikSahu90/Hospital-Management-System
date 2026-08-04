package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.PaymentRequest;
import hospital.management.backend.dto.response.PaymentResponse;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Payment;
import hospital.management.backend.enums.BillingStatus;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PaymentRepository;
import hospital.management.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BillingRepository billingRepository;

    @Override
    public PaymentResponse create(Long billId, PaymentRequest request) {
        Billing bill = billingRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        BigDecimal paid = paymentRepository.findByBillId(billId).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = bill.getTotalAmount();
        if (total == null) {
            total = bill.getConsultationFee().add(bill.getMedicineCharges()).add(bill.getOtherCharges());
        }
        if (paid.add(request.getAmount()).compareTo(total) > 0) {
            throw new IllegalArgumentException("Payment exceeds the bill total");
        }

        Payment payment = Payment.builder()
                .bill(bill)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .build();
        Payment saved = paymentRepository.save(payment);
        BigDecimal newPaid = paid.add(request.getAmount());
        bill.setStatus(newPaid.compareTo(total) == 0 ? BillingStatus.PAID : BillingStatus.PARTIALLY_PAID);
        billingRepository.save(bill);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByBill(Long billId) {
        if (!billingRepository.existsById(billId)) {
            throw new IllegalArgumentException("Bill not found");
        }
        return paymentRepository.findByBillId(billId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse findById(Long id) {
        return paymentRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getBill().getId(), payment.getAmount(),
                payment.getPaymentMethod(), payment.getPaidAt());
    }
}