-- Seed: medicines
USE hospital_management;

INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
    ('Cipla Distributors',       'Manoj Rao',   '9900000001', 'orders@cipladist.example',    'Industrial Area, Kolkata'),
    ('Sun Pharma Regional Hub',  'Kavita Iyer', '9900000002', 'orders@sunpharmahub.example', 'Sector 5, Kolkata');

INSERT INTO medicines (supplier_id, name, manufacturer, unit_price) VALUES
    (1, 'Paracetamol 500mg', 'Cipla',       2.50),
    (2, 'Amoxicillin 250mg', 'Sun Pharma',  5.00),
    (2, 'Atorvastatin 10mg', 'Sun Pharma',  8.75),
    (1, 'Ibuprofen 400mg',   'Cipla',       3.20);

INSERT INTO inventory (medicine_id, stock_quantity, reorder_level, expiry_date) VALUES
    (1, 500, 50, '2027-06-30'),
    (2, 200, 30, '2027-01-15'),
    (3, 150, 20, '2026-12-31'),
    (4, 300, 40, '2027-03-20');
