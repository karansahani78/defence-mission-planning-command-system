# 🛡️ Mission Planning and Command System (MPCS)

A **defence‑grade backend system** that models real‑world **mission planning, command authority, asset management, and secure telemetry pipelines**.
This project is intentionally designed to reflect **real military / defence command systems**, focusing on **security, correctness, auditability, and observability**.

---

## 📌 Project Overview

The **Mission Planning and Command System (MPCS)** provides a secure, role‑driven platform to:

* Plan and approve missions with strict lifecycle enforcement
* Manage defence assets with readiness and sustainment constraints
* Issue, execute, and audit operational commands
* Enforce **command authority** using role‑based access control (RBAC)
* Produce **telemetry events** for audit trails, observability, and analytics
* Support **monitoring & visualization** using Prometheus + Grafana

The system prevents **unsafe state transitions**, **unauthorized actions**, and **inconsistent operational flows**, making it suitable for **high‑criticality environments**.

---

## 🏗️ High‑Level System Design (HLD)

### 🔹 Logical Architecture

```
┌──────────────────────┐
│  Client Layer        │
│  (Postman / UI)      │
└──────────┬───────────┘
           │ JWT Authentication
           ▼
┌──────────────────────┐
│ Spring Boot API      │
│                      │
│ Controllers (RBAC)   │
│ Services (Rules)     │
│ Security (JWT)       │
│ Validation           │
└──────────┬───────────┘
           │ JPA / Hibernate
           ▼
┌──────────────────────┐
│ PostgreSQL Database  │
│ (Mission, Asset,    │
│  Command, User)     │
└──────────┬───────────┘
           │ Telemetry Events
           ▼
┌──────────────────────┐
│ Apache Kafka         │
│ (command.telemetry) │
└──────────┬───────────┘
           │ Metrics / Events
           ▼
┌──────────────────────┐
│ Observability Stack  │
│ Prometheus + Grafana │
└──────────────────────┘
```

---

### 🔹 Architectural Principles

* **Layered Architecture** (Controller → Service → Repository)
* **Stateless Security** using JWT
* **Strict RBAC enforcement** at controller & service level
* **Event‑driven telemetry** via Kafka
* **Observability‑first design** (metrics, logs, telemetry)
* **Defensive programming** for invalid state transitions

---

## 🔐 Security Model

### Authentication

* Stateless **JWT‑based authentication**
* Token required on every secured endpoint

```
Authorization: Bearer <JWT_TOKEN>
```

### Authorization (RBAC)

| Role                    | Responsibility                                    |
| ----------------------- | ------------------------------------------------- |
| ROLE_SYSTEM_ADMIN       | System authority, user creation, mission approval |
| ROLE_MISSION_PLANNER    | Mission planning, asset assignment                |
| ROLE_MISSION_COMMANDER  | Command issuance & execution                      |
| ROLE_OPERATIONS_ANALYST | Read‑only operational access                      |
| ROLE_SYSTEM             | Internal/system‑level automation                  |

Unauthorized access attempts are **blocked and audited**.

---

## 📸 API Evidence (Postman Screenshots)

![aborted mission](https://github.com/user-attachments/assets/eb6be52a-90d3-44df-8dcb-bfbb045dbe65)
![completed mission](https://github.com/user-attachments/assets/dd83e448-45dc-4659-9a4e-fdd124d45c73)
![in_progress mission](https://github.com/user-attachments/assets/8e893735-ddd5-4497-a94c-6021873149f2)
![approved Mission](https://github.com/user-attachments/assets/727df37a-6413-427c-8875-4839006a9a79)
![Draft Missio![telemetryEventProduced](https://github.com/user-attachments/assets/b77eb01d-9bc7-47ed-bfe4-44221fc5176a)
n](https://github.com/user-attachments/assets/5a5153ed-04e7-4384-ae86-69fd56decc9c)
![assignedAssetToaMission](https://github.com/user-attachments/assets/a911cffa-8312-49e2-bc4b-9352a1a6f281)
![allAssets](https://github.com/user-attachments/assets/55842748-4dd0-4de9-a97f-fc0bea28d193)
![missions](https://github.com/user-attachments/assets/e4532bc7-2441-4a10-b06f-10a2438525a8)
![getPersonnel](https://github.com/user-attachments/assets/7d7e5a46-14b5-4de2-9168-88affab9c6d6)
![Login](https://github.com/user-attachments/assets/972787a2-5bbd-4816-b01b-6e178973d95e)


## 👤 User APIs

### Register User

**POST** `/api/v1/users/register`

**Authorization:** `ROLE_SYSTEM_ADMIN`

**Request**

```json
{
  "username": "commander1",
  "email": "commander1@defence.local",
  "password": "StrongPassword123",
  "role": "ROLE_MISSION_COMMANDER"
}
```

**Response – 201 Created**

```json
{
  "id": 1,
  "username": "commander1",
  "email": "commander1@defence.local",
  "role": "ROLE_MISSION_COMMANDER",
  "enabled": true,
  "accountNonLocked": true,
  "createdAt": "2026-01-08T10:15:30"
}
```

---

### Login

**POST** `/api/v1/users/login`

**Request**

```json
{
  "username": "commander1",
  "password": "StrongPassword123"
}
```

**Response – 200 OK**

```json
{
  "accessToken": "<JWT_TOKEN>",
  "tokenType": "Bearer"
}
```

---

### Get Current User

**GET** `/api/v1/users/me`

**Authorization:** Authenticated

**Response – 200 OK**

```json
{
  "id": 1,
  "username": "commander1",
  "email": "commander1@defence.local",
  "role": "ROLE_MISSION_COMMANDER",
  "enabled": true,
  "accountNonLocked": true,
  "createdAt": "2026-01-08T10:15:30"
}
```

---

## 🎯 Mission APIs

### Create Mission

**POST** `/api/v1/missions`

**Authorization:** `ROLE_MISSION_PLANNER`

**Request**

```json
{
  "missionCode": "MIS-SAR-001",
  "missionName": "Search and Rescue",
  "missionObjective": "Recover personnel from hostile terrain",
  "priority": "CRITICAL",
  "securityLevel": "SECRET",
  "estimatedDurationHours": 6,
  "estimatedRangeKm": 300,
  "minAssetReadiness": 80,
  "minRequiredSustainmentLevel": 50,
  "startTime": "2026-01-10T06:00:00",
  "plannedEndTime": "2026-01-10T12:00:00",
  "operationArea": "Northern Sector"
}
```

**Response – 201 Created**

```json
{
  "id": 10,
  "missionCode": "MIS-SAR-001",
  "status": "DRAFT",
  "priority": "CRITICAL",
  "securityLevel": "SECRET",
  "createdBy": "planner1",
  "createdAt": "2026-01-08T11:00:00"
}
```

---

## 🛩️ Asset APIs

### Create Asset

**POST** `/api/v1/assets`

**Authorization:** `ROLE_SYSTEM_ADMIN`

**Request & Response**

> 🖼️ *Insert Postman screenshot here*

---

## 🧾 Command APIs

### Issue Command

**POST** `/api/v1/commands`

**Authorization:** `ROLE_MISSION_COMMANDER`

**Request & Response**

> 🖼️ *Insert Postman screenshot here*

---

## 📡 Telemetry & Observability

* Every command lifecycle event produces a **Kafka telemetry event**
* Events are consumable by analytics, SIEM, or monitoring systems
* Prometheus scrapes application metrics
* Grafana visualizes:

  * Mission status distribution
  * Command success/failure rates
  * Emergency command spikes
  * JVM & API performance

---

## 🏆 Why This Project Stands Out

* Defence‑grade workflow enforcement
* Strong RBAC & JWT security
* Clean domain‑driven design
* Event‑driven telemetry pipeline
* Observability with Grafana & Prometheus
* Recruiter‑ready, real‑world architecture

---

**Author:** Karan Sahani
**Role:** Backend Engineer (Java | Spring Boot | Security | Kafka)
