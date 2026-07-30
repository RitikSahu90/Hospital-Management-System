# System Architecture

## Architecture Style

The Hospital Management System follows a cloud-native, layered architecture designed for scalability, maintainability, and enterprise-grade security.

---

## System Layers

```
+-------------------------------------------------------+
|                 Presentation Layer                    |
|          (React, TypeScript, Tailwind CSS)            |
+-------------------------------------------------------+
                           │
                           ▼
+-------------------------------------------------------+
|                      API Layer                        |
|               (Spring Boot REST APIs)                 |
+-------------------------------------------------------+
                           │
                           ▼
+-------------------------------------------------------+
|                   Business Layer                      |
|           (Services, Security, Validation)            |
+-------------------------------------------------------+
                           │
                           ▼
+-------------------------------------------------------+
|                  Persistence Layer                    |
|             (Spring Data JPA, Hibernate)              |
+-------------------------------------------------------+
                           │
                           ▼
+-------------------------------------------------------+
|                      Database                         |
|                       (MySQL)                         |
+-------------------------------------------------------+
                           │
                           ▼
+-------------------------------------------------------+
|                    Cloud Storage                      |
|                       (AWS S3)                        |
+-------------------------------------------------------+
```

### Layer Breakdown

1. **Presentation Layer**
   - **React**: Single Page Application (SPA) library for building modern, reactive user interfaces.
   - **TypeScript**: Static typing over JavaScript to ensure compile-time error checking and maintainability.
   - **Tailwind CSS**: Utility-first CSS framework for building clean, responsive design components.

2. **API Layer**
   - **Spring Boot REST APIs**: Entry point for HTTP/HTTPS requests, handling endpoint routing, request mapping, and response serialization.

3. **Business Layer**
   - **Services**: Encapsulates core business logic, domain rules, and transactional boundaries.
   - **Security**: Handles request filtering, token parsing, and security context initialization.
   - **Validation**: Enforces request payload sanitization and constraint validation before processing.

4. **Persistence Layer**
   - **Spring Data JPA & Hibernate**: Object-Relational Mapping (ORM) framework providing automated repository implementations and efficient DB operations.

5. **Database**
   - **MySQL**: Relational database server storing structured data across system modules (Users, Appointments, Billing, Medical Records).

6. **Cloud Storage**
   - **AWS S3**: Scalable object storage used for storing external binary files such as lab reports, patient media, and document attachments.

---

## Authentication & Authorization Flow

```
+--------------------------+
|    JWT Authentication    |
+--------------------------+
             │
             ▼
+--------------------------+
| Role-Based Access Control|
|          (RBAC)          |
+--------------------------+
             │
             ▼
+--------------------------+
|      Protected APIs      |
+--------------------------+
```

- **JWT Authentication**: Users obtain a signed JSON Web Token upon successfully authenticating credentials.
- **Role-Based Access Control (RBAC)**: Requests are verified against assigned roles (`ADMIN`, `DOCTOR`, `PATIENT`, `PHARMACIST`) to enforce authorization boundaries.
- **Protected APIs**: Endpoints enforce role-based annotations and token validation filters prior to executing controllers.

---

## Deployment Architecture

```
+-------------------+
| React Frontend    |
+-------------------+
          │
          ▼
+-------------------+
| Nginx Web Server  |
+-------------------+
          │
          ▼
+-------------------+
| Docker Containers |
+-------------------+
          │
          ▼
+-------------------+
| Kubernetes Cluster|
+-------------------+
          │
          ▼
+-------------------+
| AWS Infrastructure|
+-------------------+
```

- **React Application**: Bundled frontend assets served by Nginx.
- **Nginx**: Operates as a reverse proxy, web server, and static file server with SSL termination.
- **Docker**: Containerizes microservices and web components for reproducible execution across environments.
- **Kubernetes**: Manages container orchestration, load balancing, automated rollouts, and self-healing deployment pods.
- **AWS Cloud**: Provides underlying cloud compute, networking (VPC), storage, and managed cloud infrastructure.
