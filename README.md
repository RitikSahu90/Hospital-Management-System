# 🏥 Hospital Management System

A full-stack, cloud-ready Hospital Management System built as a **modular Spring Boot monolith** with a React frontend, containerized with Docker, and deployable to Kubernetes with CI/CD via GitHub Actions.

---

## 📖 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Core Modules](#core-modules)
- [API Overview](#api-overview)
- [Database Schema (High Level)](#database-schema-high-level)
- [Getting Started](#getting-started)
- [Running with Docker](#running-with-docker)
- [Deploying to Kubernetes](#deploying-to-kubernetes)
- [CI/CD Pipeline](#cicd-pipeline)
- [Team Roles](#team-roles)
- [Roadmap](#roadmap)

---

## Overview

This system digitizes core hospital operations: patient registration, doctor management, appointment scheduling, billing, and pharmacy inventory — with role-based access control, file/report storage, and BI dashboards.

For a one-week build, the scope is intentionally layered:

| Priority | Component | Approach |
|---|---|---|
| ✅ Full implementation | React, Spring Boot, JWT Auth, MySQL, AWS S3, Docker, Kubernetes, GitHub Actions | Core, production-style |
| ⚙️ Light implementation | FHIR | FHIR-inspired resource endpoints, not a full FHIR server |
| 📊 Separate track | Power BI | Connected directly to MySQL after core app is stable |

---

## Architecture

```
                                USER
                                  │
                                  │ HTTPS
                                  ▼
                     ┌────────────────────────┐
                     │    React Frontend      │
                     │  (Bootstrap + Chart.js)│
                     └────────────┬───────────┘
                                  │
                            Axios REST API
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │ Spring Boot REST API    │
                    │ Spring Security + JWT   │
                    └──────────┬──────────────┘
                               │
       ┌───────────────────────┼────────────────────────┐
       │                       │                        │
       ▼                       ▼                        ▼
 Authentication          Business Logic            File Storage
 (JWT, Roles)      (Patients, Doctors, etc.)         AWS S3
       │                       │
       └───────────────┬───────┘
                        ▼
                 Spring Data JPA
                        │
                        ▼
                  MySQL Database
                        │
                        ▼
                HL7 FHIR Resources
                        │
                        ▼
                  Power BI Reports
```

**Deployment layer:**

```
Docker Containers  →  Kubernetes Cluster
 ├── Frontend Pod
 ├── Backend Pod
 └── MySQL Pod

GitHub → GitHub Actions → Docker Build → Deploy to Kubernetes
```

**Key principle:** the frontend never talks to the database directly — every request flows through the Spring Boot API.

```
User → React → Axios → Spring Boot API → MySQL / S3
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React.js, Bootstrap, Axios, Chart.js |
| Backend | Spring Boot, Spring Security, Hibernate/JPA |
| Auth | JWT, BCrypt |
| Database | MySQL |
| File Storage | AWS S3 |
| Interoperability | FHIR-inspired REST resources |
| Analytics | Power BI (connected to MySQL) |
| Containerization | Docker, Docker Compose |
| Orchestration | Kubernetes (Minikube/Kind for local dev) |
| CI/CD | GitHub Actions |

---

## Project Structure

```text
hospital-management-system/
│
├── frontend/                 # React application
│   ├── src/
│   │   ├── components/       # Reusable UI components
│   │   ├── pages/            # Login, Dashboard, Patients, Appointments, Billing, Pharmacy
│   │   ├── services/         # Axios API clients
│   │   └── context/          # Auth context, token storage
│   └── package.json
│
├── backend/                  # Spring Boot application
│   ├── src/main/java/com/hms/
│   │   ├── config/           # Security, CORS, S3, Swagger config
│   │   ├── controller/       # REST controllers (Patient, Doctor, Appointment, Billing, Pharmacy, Auth, FHIR)
│   │   ├── service/          # Business logic
│   │   ├── repository/       # Spring Data JPA repositories
│   │   ├── model/            # JPA entities
│   │   ├── dto/               # Request/response DTOs
│   │   ├── security/         # JWT filter, provider, roles
│   │   └── exception/        # Global exception handling
│   └── pom.xml
│
├── database/                  # SQL scripts (schema, seed data, migrations)
│
├── docker/                    # Dockerfiles & docker-compose.yml
│
├── k8s/                        # Kubernetes manifests
│   ├── frontend-deployment.yaml
│   ├── backend-deployment.yaml
│   ├── mysql-deployment.yaml
│   ├── frontend-service.yaml
│   ├── backend-service.yaml
│   ├── mysql-service.yaml
│   ├── configmap.yaml
│   └── secret.yaml
│
├── docs/                        # API docs, ERD, user guide
│
├── .github/
│   └── workflows/
│       └── ci.yml               # GitHub Actions pipeline
│
└── README.md
```

---

## Core Modules

| Module | Description |
|---|---|
| **Auth** | Login, JWT issuance, role-based access (Admin, Doctor, Receptionist, Patient) |
| **Patients** | Registration, profile, medical history, report uploads (S3) |
| **Doctors** | Profiles, specializations, availability |
| **Appointments** | Booking, rescheduling, status tracking |
| **Billing** | Invoice generation, payment status |
| **Pharmacy** | Medicine inventory, stock levels, dispensing |
| **FHIR Layer** | Simplified `/fhir/Patient/{id}`-style endpoints for interoperability demos |
| **Reporting** | Power BI dashboards reading directly from MySQL |

---

## API Overview

Example endpoints (REST, JSON, JWT-protected unless noted):

```
POST   /api/auth/login              # Public - returns JWT
POST   /api/auth/register           # Admin only

GET    /api/patients                # List patients
POST   /api/patients                # Register patient
GET    /api/patients/{id}
POST   /api/patients/{id}/reports   # Upload report → stored in S3, URL saved in MySQL

GET    /api/doctors
POST   /api/doctors

GET    /api/appointments
POST   /api/appointments
PUT    /api/appointments/{id}/status

GET    /api/billing/{patientId}
POST   /api/billing

GET    /api/pharmacy/medicines
POST   /api/pharmacy/medicines

GET    /fhir/Patient/{id}           # FHIR-inspired resource format
```

### Auth flow

```
User Login → Spring Security → Verify Password (BCrypt) → Generate JWT
→ Return Token → React stores Token → Future requests include
Authorization: Bearer <token> → Spring Security validates JWT
```

### File upload flow

```
Patient uploads report → Spring Boot → AWS SDK → S3 Bucket
→ Returns URL → MySQL stores only the URL (not the file)
```

---

## Database Schema (High Level)

Core tables (see `database/` for full DDL):

- `users` (id, username, password_hash, role)
- `patients` (id, name, dob, contact, report_url, ...)
- `doctors` (id, name, specialization, availability)
- `appointments` (id, patient_id, doctor_id, date, status)
- `bills` (id, patient_id, amount, status, created_at)
- `medicines` (id, name, stock_quantity, price)

Hibernate maps entities like `Patient.java` directly to the `patients` table via JPA annotations.

---

## Getting Started

### Prerequisites

- Java 17+, Maven
- Node.js 18+, npm
- MySQL 8+
- AWS account (for S3) — or use a local mock (e.g. LocalStack) for dev
- Docker & Docker Compose
- kubectl + Minikube/Kind (for Kubernetes)

### Local setup (without Docker)

```bash
# Backend
cd backend
./gradlew bootRun

# Frontend
cd frontend
npm install
npm run dev
```

The application now uses Spring Boot configuration properties with environment variable overrides.

Set environment variables as needed:
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION_MS`
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_S3_BUCKET`
- `APP_PROFILE`, `APP_ENVIRONMENT`

Example:

```bash
export APP_PROFILE=dev
export DB_URL=jdbc:h2:mem:backenddb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
export DB_USERNAME=sa
export DB_PASSWORD=
export JWT_SECRET=local-dev-secret
export AWS_S3_BUCKET=demo-hospital-bucket
```

### Production profile

For production deployments, activate the prod profile:

```bash
export APP_PROFILE=prod
export SPRING_PROFILES_ACTIVE=prod
```

This loads [backend/src/main/resources/application-prod.properties](backend/src/main/resources/application-prod.properties) and keeps secrets externalized from source code.

### Future secret-store integration

The configuration is prepared for future integration with:
- AWS Secrets Manager
- HashiCorp Vault

These services can override the same environment-backed properties without changing application code.

---

## Running with Docker

```bash
docker-compose up --build
```

This spins up three containers:
- `frontend` — React app served via Nginx
- `backend` — Spring Boot app
- `mysql` — MySQL database with seed data

---

## Deploying to Kubernetes

```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/mysql-deployment.yaml -f k8s/mysql-service.yaml
kubectl apply -f k8s/backend-deployment.yaml -f k8s/backend-service.yaml
kubectl apply -f k8s/frontend-deployment.yaml -f k8s/frontend-service.yaml
```

Kubernetes automatically restarts crashed pods:

```
Backend Pod → Crash → Kubernetes → Restart Automatically
```

---

## CI/CD Pipeline

`.github/workflows/ci.yml` runs on every push:

```
Push Code → GitHub Actions → Build → Run Tests
→ Build Docker Image → Deploy to Kubernetes
```

---

## Team Roles

| Member | Focus |
|---|---|
| Backend Engineer | Spring Boot, JWT, Security, REST APIs, FHIR endpoints |
| Frontend Engineer | React, Bootstrap, Dashboard, Axios, Chart.js |
| Module Developer (1) | Patient module, Doctor module, AWS S3 integration |
| Module Developer (2) | Appointment, Billing, Pharmacy modules |
| DevOps Engineer | Docker, Kubernetes, GitHub Actions, testing, documentation, deployment |

---

## Roadmap

- [ ] Add automated test coverage (JUnit + React Testing Library)
- [ ] Expand FHIR endpoints toward full resource compliance
- [ ] Add notification service (email/SMS for appointments)
- [ ] Add audit logging for compliance
- [ ] Production-grade secrets management (Vault / AWS Secrets Manager)
