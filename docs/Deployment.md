# Deployment Guide

## Overview

This guide details the deployment pipeline for the Hospital Management System, spanning local development setup, containerization, Kubernetes orchestration, automated CI/CD workflows, and cloud storage/analytics integrations.

---

## 1. Local Development Architecture

```
+-----------------------------------+
|         React Frontend            |
|     (Vite / Node.js Dev Server)   |
+-----------------------------------+
                  │
                  ▼ (Port 8080 / HTTP)
+-----------------------------------+
|       Spring Boot Backend         |
|         (Embedded Tomcat)         |
+-----------------------------------+
                  │
                  ▼ (Port 3306 / JDBC)
+-----------------------------------+
|          MySQL Database           |
|        (Local Server Instance)    |
+-----------------------------------+
```

- **Frontend**: Local Node.js environment executing the React client application (hot-reloading enabled).
- **Backend**: Spring Boot REST application running on an embedded Tomcat server with live reload / Spring DevTools.
- **Database**: Local MySQL 8 database instance configured with database schemas and initial seed data.

---

## 2. Docker Containerization Strategy

The application components are isolated into lightweight, reproducible multi-stage Docker containers.

```
+--------------------------+  +--------------------------+  +--------------------------+
|    Frontend Container    |  |    Backend Container     |  |    Database Container    |
|  (Nginx Alpine + Assets) |  |   (OpenJDK / Java Runtime|  |  (Official MySQL 8 Image)|
+--------------------------+  +--------------------------+  +--------------------------+
```

### Multi-stage Docker Builds
- **Frontend Container**: Multi-stage build compiling React TypeScript code into static assets served via lightweight **Nginx Alpine**.
- **Backend Container**: Multi-stage Maven/OpenJDK build yielding an optimized runnable JAR executed in a minimal JRE base image.
- **Database Container**: Official `mysql:8.0` image pre-configured with volume mounts for persistent data (`/var/lib/mysql`).

---

## 3. Kubernetes Orchestration

Production workloads are orchestrated using Kubernetes manifests categorized across five core primitive kinds:

```
+-----------------------------------------------------------------------------------+
|                                 Ingress Controller                                |
|                        (TLS Termination & Domain Routing)                         |
+-----------------------------------------------------------------------------------+
                                          │
                                          ▼
+-----------------------------------------------------------------------------------+
|                                    Services                                       |
|                    (ClusterIP / NodePort Load Balancing)                          |
+-----------------------------------------------------------------------------------+
                                          │
                      ┌───────────────────┴───────────────────┐
                      ▼                                       ▼
+-------------------------------------------+ +-------------------------------------+
|            Frontend Deployment            | |          Backend Deployment         |
|             (Replicated Pods)             | |          (Replicated Pods)          |
+-------------------------------------------+ +-------------------------------------+
                      │                                       │
                      └───────────────────┬───────────────────┘
                                          ▼
+-----------------------------------------------------------------------------------+
|                              ConfigMaps & Secrets                                 |
|               (App Properties, DB Credentials, AWS S3 API Keys)                  |
+-----------------------------------------------------------------------------------+
```

- **Deployments**: Manages replica sets, declarative updates, health checks (liveness/readiness probes), and auto-scaling pods.
- **Services**: Exposes internal cluster networking via `ClusterIP` for inter-pod routing and `LoadBalancer` for external entry.
- **Ingress**: Handles domain name routing, path rules (`/api/*` vs `/`), and HTTPS/TLS termination.
- **ConfigMaps**: Stores non-sensitive runtime configurations (database URLs, application profiles, log levels).
- **Secrets**: Securely injects base64-encoded sensitive keys (MySQL root passwords, JWT secret tokens, AWS S3 credentials).

---

## 4. Continuous Integration & Continuous Deployment (CI/CD)

Automated deployments are triggered on repository events via GitHub Actions pipelines.

```
+-----------------------+
|  GitHub Actions Push  |
+-----------------------+
            │
            ▼
+-----------------------+
|   Docker Build Step   |
+-----------------------+
            │
            ▼
+-----------------------+
|   Docker Push (ECR)   |
+-----------------------+
            │
            ▼
+-----------------------+
| Kube Deployment Rollout|
+-----------------------+
```

1. **GitHub Actions**: Pipeline triggered upon commit push or pull request to primary branches (`main` / `release`).
2. **Docker Build**: Executes unit tests and builds Docker image tags using `git SHA` and `latest` tags.
3. **Docker Push**: Authenticates and pushes signed container images to the container registry (e.g., AWS ECR or Docker Hub).
4. **Kubernetes Deployment**: Updates `kubectl` contexts and executes rolling restarts/deployments against target K8s clusters.

---

## 5. Cloud Integration & Business Intelligence

```
+-----------------------+         +-----------------------+
|        AWS S3         |         |       Power BI        |
| (Secure Media/Reports)|         | (Analytics Dashboard) |
+-----------------------+         +-----------------------+
```

- **AWS S3 (Report Storage)**:
  - Receives uploaded medical documents, lab scan PDFs, and imaging outputs directly via pre-signed S3 URLs.
  - Bucket access policies restrict public exposure while retaining secure direct access via API authentication.

- **Power BI (Analytics Dashboard)**:
  - Connects securely to replica database nodes or data warehouses.
  - Renders real-time executive dashboards for hospital metrics, patient admission volumes, billing summaries, and medicine consumption analytics.
