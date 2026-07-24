USE hospital_management;

INSERT INTO appointments
(
    patient_id,
    doctor_id,
    appointment_date,
    appointment_time,
    status,
    reason
)
VALUES
(1,1,'2026-07-25','09:30:00','SCHEDULED','Routine cardiac checkup'),
(2,2,'2026-07-23','10:15:00','COMPLETED','Knee pain follow-up'),
(3,1,'2026-07-24','14:00:00','CONFIRMED','Chest discomfort');