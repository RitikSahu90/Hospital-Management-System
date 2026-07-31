# 🏥 Hospital Management System

## 📌 Project Overview

The Hospital Management System is a full-stack web application developed to digitize and manage hospital operations. The system provides modules for patient management, doctor management, appointments, billing, pharmacy, authentication, and administration.

This project follows Agile (Kanban) methodology using Jira for project management.

---

# 👥 Team Members & Responsibilities

## 👨‍💻 Member 1 – Backend Lead

### Responsibilities

- Authentication & Authorization
- Spring Security
- JWT
- REST APIs
- API Integration
- Backend Code Review
- Merge Backend Changes

### Technologies

- Java
- Spring Boot
- Spring Security
- JWT
- MySQL
- Maven

---

## 🎨 Member 2 – Frontend Lead

### Responsibilities

- React UI Development
- Dashboard
- Login/Register Pages
- Responsive Design
- API Integration
- UI Improvements

### Technologies

- React.js
- Bootstrap
- Axios
- Chart.js

---

## 👨‍⚕️ Member 3 – Patient & Doctor Module

### Responsibilities

- Patient Management
- Doctor Management
- CRUD Operations
- Search
- Validation
- Database Integration

---

## 📅 Member 4 – Appointment & Billing

### Responsibilities

- Appointment Scheduling
- Billing
- Invoice Generation
- Payment Module
- Reports

---

## 🚀 Member 5 – DevOps & QA

### Responsibilities

- Docker
- Docker Compose
- GitHub Actions
- Testing
- Documentation
- Deployment
- Bug Fixes

---

# 📁 Project Structure

backend/
frontend/
database/
docker/
docs/

---

# 🌿 Git Workflow

Every member should create their own feature branch.

Example:

feature/authentication

feature/dashboard

feature/patient-management

feature/appointment

feature/devops

Never work directly on the main branch.

Workflow:

1. Pull latest changes
2. Create feature branch
3. Develop feature
4. Commit changes
5. Push branch
6. Create Pull Request
7. Code Review
8. Merge into main

---

# 📌 Jira Workflow

Backlog

↓

To Do

↓

In Progress

↓

Code Review

↓

Testing

↓

Done

Each Story should move through these stages.

---

# 💻 Development Rules

<<<<<<< HEAD
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
=======
- Follow clean coding principles.
- Write meaningful commit messages.
- Keep commits small and focused.
- Review code before merging.
- Resolve merge conflicts immediately.
- Test before creating a Pull Request.
>>>>>>> fa0b885e5e8d4d52eb59f89decfc949f4b3e8056

---

# 📅 Development Timeline

## Day 1

- Project Setup
- Repository Setup
- Environment Configuration
- Database Setup

---

## Day 2–5

Parallel Development

- Backend
- Frontend
- Patient Module
- Appointment Module
- Docker

---

## Day 6

Integration

- Connect Frontend & Backend
- API Testing
- Database Validation

---

## Day 7

Final Testing

- Bug Fixes
- Documentation
- Presentation
- Final Deployment

---

# 🛠️ Technology Stack

Frontend

- React.js
- Bootstrap
- Axios
- Chart.js

Backend

- Java
- Spring Boot
- Spring Security
- JWT

Database

- MySQL

DevOps

- Docker
- Docker Compose
- GitHub Actions

Tools

- Jira
- GitHub
- Postman
- VS Code / IntelliJ IDEA

---

# 📌 Coding Standards

- Follow Java naming conventions.
- Follow React component structure.
- Use RESTful API standards.
- Write reusable components.
- Avoid duplicate code.
- Maintain proper folder structure.

---

# 📖 Documentation

Every completed module must include:

- API Documentation
- Screenshots
- Testing Results
- README Updates

---

# 🎯 Goal

Deliver a fully functional Hospital Management System with:

- Secure Authentication
- Responsive User Interface
- REST APIs
- Database Integration
- Dockerized Deployment
- Complete Documentation
