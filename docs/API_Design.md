# REST API Design

## Authentication

POST /api/auth/login

POST /api/auth/register

GET /api/auth/profile

---

## Patients

GET /api/patients

GET /api/patients/{id}

POST /api/patients

PUT /api/patients/{id}

DELETE /api/patients/{id}

---

## Doctors

GET /api/doctors

POST /api/doctors

PUT /api/doctors/{id}

DELETE /api/doctors/{id}

---

## Appointments

GET /api/appointments

POST /api/appointments

PUT /api/appointments/{id}

DELETE /api/appointments/{id}

---

## Billing

GET /api/bills

POST /api/bills

---

## Reports

POST /api/reports/upload

GET /api/reports/{id}
