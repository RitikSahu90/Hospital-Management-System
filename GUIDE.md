# 🛠️ Setup Guide — Build Order

Follow these steps in order. Don't skip ahead — each step depends on the previous one working.

---

## Step 1 — Environment Setup (one time only)

Install and verify each of these before writing any code:

```bash
java -version      # need 17+
mvn -version
node -version       # need 18+
mysql --version     # need 8+
docker --version
```

Also create a free/trial AWS account (for S3) — you won't need it until later.

---

## Step 2 — Database (do this first)

1. Install MySQL locally, or run it via Docker.
2. Load the schema and sample data:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/seed.sql
   ```
3. Verify it worked:
   ```sql
   USE hospital_management;
   SHOW TABLES;
   SELECT * FROM patients;
   ```
   You should see 3 sample patients. If so, move on.

---

## Step 3 — Backend (Spring Boot) — build inside `backend/`

1. Generate a project at [start.spring.io](https://start.spring.io) with:
   Spring Web, Spring Data JPA, Spring Security, MySQL Driver, Validation.
2. Point `application.yml` at your local `hospital_management` database.
3. Write JPA entities that match the schema (`Patient`, `Doctor`, `Appointment`, `Bill`, `Medicine`, etc.).
4. Build **one module fully, end-to-end, first** — e.g. Patients:
   entity → repository → service → controller.
   Prove `GET /api/patients` works in Postman before touching anything else.
5. Add JWT authentication only once basic CRUD is working — don't debug auth and CRUD at the same time.
6. Then repeat the pattern for Doctors, Appointments, Billing, Pharmacy.

---

## Step 4 — Frontend (React) — build inside `frontend/`

1. `npx create-react-app .` (run inside the `frontend/` folder), or use Vite.
2. Build the **Login page first**, wired to `/api/auth/login`.
3. Then a simple Patients list page hitting `/api/patients` — this proves frontend↔backend are actually connected.
4. Only after that connection works, build out Doctors, Appointments, Billing, Pharmacy pages.

---

## Step 5 — Docker

Only do this once backend and frontend both run locally without Docker.

- Add a `Dockerfile` to `backend/` and `frontend/`.
- Add a `docker-compose.yml` at the project root (or in a `docker/` folder) wiring together `frontend`, `backend`, and `mysql`.
- Run `docker-compose up --build` and confirm everything still works the same as local.

---

## Step 6 — Kubernetes, CI/CD, S3, FHIR, Power BI (last)

These sit on top of a working app, in priority order:

1. **AWS S3** — for patient report uploads (store the URL only in MySQL, per the schema).
2. **Kubernetes** — containerize with the k8s manifests, deploy locally with Minikube/Kind first.
3. **GitHub Actions** — automate build → test → deploy on every push.
4. **FHIR-inspired endpoints** — lightweight, not a full FHIR server.
5. **Power BI** — connect directly to MySQL once there's real data to visualize.

---

## Rule of thumb

Don't move to the next step until the current one demonstrably works end-to-end. Database → one backend module → frontend talking to that module → then scale out.
