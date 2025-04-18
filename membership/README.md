# Customer Service - Spring Boot Application

This is a Spring Boot application for managing customer data with full CRUD support, OpenAPI documentation, dynamic tier classification, and in-memory H2 database.

---

## ✅ Features

- Create, retrieve, update, and delete customer records.
- Retrieve customers by name or email.
- Dynamic tier assignment (Silver, Gold, Platinum) based on annual spend and last purchase date.
- OpenAPI documentation with Swagger UI.
- H2 in-memory database for testing.
- Bean validation for required fields (`name`, `email`).
- Logging using SLF4J.

---

## 📦 Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database (can be replaced with MySQL/PostgreSQL)
- Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- Hibernate Validator
- SLF4J

---

## 🚀 How to Build & Run

### 🛠 Prerequisites

- Java 17+
- Maven 3.8+
- Optional: Eclipse/IntelliJ

### 🔧 Build

```bash
mvn clean install
```

### ▶️ Run

```bash
mvn spring-boot:run
```

Or using the JAR file:

```bash
java -jar target/customer-service-0.0.1-SNAPSHOT.jar
```

---

## 🔍 API Endpoints & Sample Requests

### 1. **Create Customer**
- **POST** `/customers`

```json
{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "annualSpend": 8500,
  "lastPurchaseDate": "2024-06-01"
}
```

### 2. **Get Customer by ID**
- **GET** `/customers/{id}`

### 3. **Get Customer by Name**
- **GET** `/customers/by-name?name=Alice Smith`

### 4. **Get Customer by Email**
- **GET** `/customers/by-email?email=alice@example.com`

### 5. **Update Customer**
- **PUT** `/customers/{id}`

```json
{
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "annualSpend": 10000,
  "lastPurchaseDate": "2024-12-01"
}
```

### 6. **Delete Customer**
- **DELETE** `/customers/{id}`

---

## 🧠 Tier Classification Rules

Tiers are dynamically assigned and **not stored** in the database:

- **Silver**: `annualSpend < 1000`
- **Gold**: `annualSpend >= 1000` and `< 10000` and purchase within **last 12 months**
- **Platinum**: `annualSpend >= 10000` and purchase within **last 6 months`

---

## 🔗 Swagger UI & OpenAPI Docs

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🗃 Access H2 Database Console

- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(leave blank)*

> Enable in `application.properties`:

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## 🔒 Validation & Error Handling

- Missing `name` or `email` returns `400 Bad Request`.
- Not found resources return `404 Not Found` with JSON error message.
- Logging is enabled via SLF4J.

---


