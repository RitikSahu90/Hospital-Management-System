-- Seed: bills, payments
USE hospital_management;

INSERT INTO bills (patient_id, appointment_id, consultation_fee, medicine_charges, other_charges, status) VALUES
    (2, 2, 600.00, 44.80, 0.00, 'PAID'),
    (1, 1, 800.00, 0.00,  0.00, 'PENDING');

INSERT INTO payments (bill_id, amount, payment_method, paid_at) VALUES
    (1, 644.80, 'UPI', '2026-07-23 11:00:00');
