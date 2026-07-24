-- Seed: departments
USE hospital_management;

INSERT INTO departments (name, code, description, status) VALUES
    ('Cardiology',   'CARD', 'Heart and cardiovascular care',        'ACTIVE'),
    ('Orthopedics',  'ORTH', 'Bone, joint, and muscle care',         'ACTIVE'),
    ('Pediatrics',   'PEDI', 'Child healthcare',                     'ACTIVE'),
    ('General Medicine', 'GMED', 'General adult medical care',      'ACTIVE');
