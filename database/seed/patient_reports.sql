USE hospital_management;

INSERT INTO patient_reports
(
    patient_id,
    title,
    report_url
)
VALUES
(
1,
'Blood Test Report',
'https://hospital-report-bucket.s3.amazonaws.com/reports/blood-report.pdf'
),
(
2,
'X-Ray Report',
'https://hospital-report-bucket.s3.amazonaws.com/reports/xray.pdf'
);
