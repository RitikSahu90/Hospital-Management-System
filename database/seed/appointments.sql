-- Seed: appointments, medical records, prescriptions
USE hospital_management;

INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason) VALUES
    (1, 1, '2026-07-25', '09:30:00', 'SCHEDULED', 'Routine cardiac checkup'),
    (2, 2, '2026-07-23', '10:15:00', 'COMPLETED',  'Knee pain follow-up'),
    (3, 1, '2026-07-24', '14:00:00', 'CONFIRMED',  'Chest discomfort');

-- Medical record for the completed appointment (id 2)
INSERT INTO medical_records (appointment_id, diagnosis, clinical_notes) VALUES
    (2, 'Mild osteoarthritis - right knee', 'Continue physiotherapy; review in 2 weeks.');

INSERT INTO prescriptions (medical_record_id, doctor_id, patient_id, notes) VALUES
    (1, 2, 2, 'Take with food to reduce stomach upset.');

INSERT INTO prescription_items (prescription_id, medicine_id, dosage, duration_days, quantity) VALUES
    (1, 4, '1 tablet after meals, twice daily', 7, 14);
