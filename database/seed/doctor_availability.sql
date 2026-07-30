USE hospital_management;

INSERT INTO doctor_availability
(
    doctor_id,
    day_of_week,
    start_time,
    end_time
)
VALUES
(1,'MON','09:00:00','13:00:00'),
(1,'WED','09:00:00','13:00:00'),
(1,'FRI','14:00:00','18:00:00'),

(2,'TUE','10:00:00','16:00:00'),
(2,'THU','10:00:00','16:00:00');