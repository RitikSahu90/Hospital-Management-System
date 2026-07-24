-- Seed: doctors
-- user_id 2 = dr.sharma, user_id 3 = dr.gupta
-- department_id: 1=Cardiology, 2=Orthopedics
USE hospital_management;

INSERT INTO doctors (user_id, department_id, doctor_code, license_number, first_name, last_name, specialization, phone, years_experience, consultation_fee, status) VALUES
    (2, 1, 'DOC-001', 'LIC-CARD-1001', 'Ritu', 'Sharma', 'Cardiology',  '9800000001', 12, 800.00, 'ACTIVE'),
    (3, 2, 'DOC-002', 'LIC-ORTH-1002', 'Anil', 'Gupta',  'Orthopedics', '9800000002', 8,  600.00, 'ACTIVE');

INSERT INTO doctor_availability (doctor_id, day_of_week, start_time, end_time) VALUES
    (1, 'MON', '09:00:00', '13:00:00'),
    (1, 'WED', '09:00:00', '13:00:00'),
    (1, 'FRI', '14:00:00', '18:00:00'),
    (2, 'TUE', '10:00:00', '16:00:00'),
    (2, 'THU', '10:00:00', '16:00:00');
