USE hospital_management;

INSERT INTO inventory
(
    medicine_id,
    stock_quantity,
    reorder_level,
    expiry_date
)
VALUES
(1,500,50,'2027-06-30'),
(2,200,30,'2027-01-15'),
(3,150,20,'2026-12-31'),
(4,300,40,'2027-03-20');