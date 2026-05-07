# Craftsmanship Community 

A web platform that connects skilled craftsmen with customers across Jordan.
Craftsmen list their services online and receive job requests directly — customers browse and request services instantly, no account needed.

 
## Tech Stack

| | |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Database | MySQL |
| Security | Spring Security + JWT |
| Frontend | React + TypeScript (separate repo) |

---

## Getting Started

### 1. Create the database
```sql
CREATE DATABASE craftsmanship_db;
```

### 2. Configure application.properties
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

For email notifications, also add:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```
> Gmail → Account Settings → Security → App Passwords → generate one for this app.
> If left blank the app still runs fine, just won't send emails.

### 3. Run
Open in IntelliJ → Load Maven Project → run `CraftsmanshipApplication.java`

Server starts at `http://localhost:8080`
Tables are created automatically on first run.


## API Endpoints

### Auth

| Method | Endpoint              | Description                  | Auth |
|--------|-----------------------|------------------------------|------|
| POST   | /api/auth/register    | Register a new craftsman     | ❌   |
| POST   | /api/auth/login       | Login and receive JWT token  | ❌   |

---

### Services

| Method | Endpoint                      | Description                            | Auth |
|--------|-------------------------------|----------------------------------------|------|
| GET    | /api/services                 | Browse all available services          | ❌   |
| GET    | /api/services/search?q=&city= | Search by name and filter by city      | ❌   |
| GET    | /api/services/{id}            | Get single service                     | ❌   |
| POST   | /api/services                 | Add a new service                      | 🔒   |
| GET    | /api/services/my              | View your own services                 | 🔒   |
| DELETE | /api/services/{id}            | Delete a service                       | 🔒   |
| PATCH  | /api/services/{id}/status     | Toggle available / unavailable         | 🔒   |
| POST   | /api/services/{id}/media      | Upload photo or video                  | 🔒   |

---

### Requests

| Method | Endpoint                          | Description                    | Auth |
|--------|-----------------------------------|--------------------------------|------|
| POST   | /api/requests                     | Submit a service request       | ❌   |
| GET    | /api/requests/my                  | View incoming requests         | 🔒   |
| PATCH  | /api/requests/{id}/status?status= | Accept or reject a request     | 🔒   |

---

### Reviews

| Method | Endpoint                    | Description                      | Auth |
|--------|-----------------------------|----------------------------------|------|
| POST   | /api/reviews                | Submit a review                  | ❌   |
| GET    | /api/reviews/service/{id}   | Get reviews for a service        | ❌   |
| GET    | /api/reviews/my             | Reviews on your services         | 🔒   |

---

### Complaints

| Method | Endpoint                              | Description                       | Auth |
|--------|---------------------------------------|-----------------------------------|------|
| POST   | /api/complaints                       | Submit a complaint                | ❌   |
| GET    | /api/complaints/my                    | View complaints on your services  | 🔒   |
| PATCH  | /api/complaints/{id}/status?status=   | Resolve or reject a complaint     | 🔒   |

---

> 🔒 Requires `Authorization: Bearer <token>` header

---

## Demo Accounts (Login)

| Account | Identifier | Password |
|---------|-----------|----------|
| Test Craftsman | test@test.com | 123456 |

*(Register your own account via `/api/auth/register`)*

## Notes

- Craftsmen are limited to **3 services** maximum
- Customers do **not** need an account to browse or request services
- Status values: requests → `pending / accepted / rejected` | complaints → `pending / resolved / rejected`
