-- Seed: patients
-- user_id 6 = patient1 (has portal login); others are walk-ins with no login
USE hospital_management;

INSERT INTO patients (user_id, patient_number, first_name, last_name, date_of_birth, gender, phone, email, address, blood_group) VALUES
    (6,    'PAT-00001', 'Aarav', 'Mehta', '1990-04-12', 'MALE',   '9811111111', 'aarav.mehta@mail.com', '12 MG Road, Kolkata',     'O+'),
    (NULL, 'PAT-00002', 'Priya', 'Nair',  '1985-11-02', 'FEMALE', '9833333333', 'priya.nair@mail.com',  '45 Park Street, Kolkata', 'A+'),
    (NULL, 'PAT-00003', 'Rohan', 'Verma', '2001-07-19', 'MALE',   '9855555555', NULL,                   '78 Salt Lake, Kolkata',   'B-');

INSERT INTO emergency_contacts (patient_id, full_name, relationship, phone) VALUES
    (1, 'Neha Mehta',   'Spouse',  '9822222222'),
    (2, 'Suresh Nair',  'Brother', '9844444444');
