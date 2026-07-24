USE hospital_management;

INSERT INTO payments
(
    bill_id,
    amount,
    payment_method,
    paid_at
)
VALUES
(
1,
644.80,
'UPI',
NOW()
);