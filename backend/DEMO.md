# HealthVerse-AI Presentation Demo Guide

This document provides a simple, repeatable sequence for demonstrating the HealthVerse-AI backend during a presentation or architectural review.

## Prerequisites
Ensure Docker Desktop is running and ports `8080-8089` and `8761` are available on your machine.

---

## Demo Sequence

### 1. Boot up the Environment
Open a terminal in the root `backend` directory and execute:
```bash
docker compose up -d
```
*Narrative*: "We are spinning up the entire microservices architecture. This brings up PostgreSQL, MongoDB, Redis, Apache Kafka, Netflix Eureka, our Spring Cloud Gateway, and all 9 distinct business microservices."

### 2. Verify Containers
Run:
```bash
docker compose ps
```
*Narrative*: "All containers are healthy and running. You can see the databases running alongside our Java applications."

### 3. Open Eureka Dashboard
Open your browser and navigate to:
`http://localhost:8761`
*Narrative*: "This is the Eureka Service Registry. As you can see, all microservices have successfully dynamically registered themselves here, enabling the API Gateway to route traffic without hardcoded IP addresses."

### 4. Authenticate User (Auth Service)
Open Postman (or cURL) and POST to `http://localhost:8080/api/auth/login`.
```json
{
  "email": "pranathi@gmail.com",
  "password": "Password123"
}
```
*Narrative*: "We authenticate through the API Gateway, which forwards to the Auth Service. We receive a cryptographically signed JWT. From now on, all requests use this Bearer token, ensuring stateless, CSRF-proof security."

### 5. View User Profile (User Service)
Make a GET request to `http://localhost:8080/users/profile` with the JWT in the `Authorization` header.
*Narrative*: "The User Service validates the JWT directly, extracting the User ID. Notice we didn't pass the ID in the URL, strictly preventing Insecure Direct Object Reference (IDOR) attacks."

### 6. Log Daily Health Data (User Service)
POST to `http://localhost:8080/users/daily-data`:
```json
{
  "steps": 10500,
  "sleepHours": 7.5,
  "hydrationLiters": 2.5
}
```
*Narrative*: "We securely log health data into the PostgreSQL database. This data forms the baseline for our AI and analytics."

### 7. View Medicine Schedule (Medicine Service)
GET `http://localhost:8080/medicines`
*Narrative*: "The Medicine service fetches our active prescriptions from its dedicated PostgreSQL schema."

### 8. Upload Medical Report (Report Service -> MongoDB)
POST `http://localhost:8080/reports/upload` with a dummy PDF file attached as `file`.
*Narrative*: "This PDF is stored securely in MongoDB, as document databases are better suited for binary metadata storage than relational databases."

### 9. Request AI Analysis (AI Analysis Service)
POST `http://localhost:8080/ai/analyze` (Requires an existing reportId).
*Narrative*: "The AI service extracts text from the uploaded medical report and passes it to an external LLM for interpretation, translating complex medical jargon into easy-to-understand wellness advice."

### 10. Generate Nutrition & Fitness Plans
GET `http://localhost:8080/nutrition`
GET `http://localhost:8080/fitness`
*Narrative*: "These services generate actionable lifestyle plans based on the user's recorded health profile."

### 11. View Wellness Summary (Wellness Service)
GET `http://localhost:8080/wellness/summary`
*Narrative*: "This aggregates multiple facets of the user's health to give a holistic view."

### 12. Demonstrate Real-time Analytics (Analytics Service -> Redis)
GET `http://localhost:8080/analytics/health-score`
*Narrative*: "The analytics service calculates a dynamic health score. To ensure low latency, this score is cached in Redis."

### 13. Demonstrate Kafka Notifications
GET `http://localhost:8080/notifications`
*Narrative*: "Whenever a user registers or uploads a report, the respective service publishes an event to Apache Kafka. The Notification Service consumes this event asynchronously and generates alerts, fully decoupling our services."

### 14. Demonstrate Security & Hardening (Phase 19)
Make a GET request to `http://localhost:8081/actuator/env`.
*Narrative*: "If an attacker bypasses the gateway, they still cannot access sensitive actuator endpoints on the internal services, as they correctly return 403 Forbidden."

### 15. Show Comprehensive Test Results
In the terminal, execute:
```powershell
.\test_phase18.ps1
```
*Narrative*: "Finally, we have a fully automated PowerShell test suite that validates the entire business flow end-to-end. As you can see, 29 out of 29 integration tests pass successfully."

---
*End of Demo.*
