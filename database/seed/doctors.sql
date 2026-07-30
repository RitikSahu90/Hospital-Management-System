USE hospital_management;

INSERT INTO doctors (
    user_id,
    department_id,
    doctor_code,
    license_number,
    first_name,
    last_name,
    specialization,
    phone,
    years_experience,
    consultation_fee,
    status
)
VALUES
(2,1,'DOC-001','LIC-CARD-1001','Ritu','Sharma','Cardiology','9800000001',12,800.00,'ACTIVE'),
(3,2,'DOC-002','LIC-ORTH-1002','Anil','Gupta','Orthopedics','9800000002',8,600.00,'ACTIVE');