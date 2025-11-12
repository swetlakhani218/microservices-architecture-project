# 🧠 Microservices Architecture Project

## 📖 Overview

This project demonstrates a complete microservices-based architecture built using Spring Boot and Spring Cloud.
It simulates a hotel rating platform where users can register, log in, rate hotels, and view detailed information aggregated from multiple microservices.

---

## ⚙️ Tech Stack

- **Language:** Java (Spring Boot 3.x)
- **Frameworks:** Spring Cloud (Eureka, Gateway, Config)
- **Databases:** MySQL, MongoDB, PostgreSQL
- **Security:** JWT Authentication
- **Resilience:** Resilience4j (Circuit Breaker, Retry, Rate Limiter)
- **API Testing:** Postman, JMeter
- **Build Tool:** Maven

---

## 🧩 Microservices Overview

| Service              | Description                                                   | Port |
| -------------------- | ------------------------------------------------------------- | ---- |
| **config-server**    | Centralized configuration management for all services         | 8085 |
| **discovery-server** | Eureka Service Registry for service discovery                 | 8761 |
| **api-gateway**      | Entry point for all client requests, handles JWT validation   | 8084 |
| **auth-service**     | Handles signup, login, and JWT token generation               | 8080 |
| **user-service**     | Manages user profiles and aggregates data from other services | 8081 |
| **rating-service**   | Manages user ratings (stored in MongoDB)                      | 8083 |
| **hotel-service**    | Manages hotel data (stored in PostgreSQL)                     | 8082 |

---

## 🔐 Authentication Flow

1. Client calls Auth-Service → `/auth/signup` or `/auth/login`
2. Auth-Service validates user (in MySQL) and issues a JWT token
3. Client sends subsequent requests to API Gateway with `Authorization: Bearer <token>`
4. Gateway validates token and routes the request to the correct service
5. Each microservice again validates the token through its own filter for added security

---

## 🕸️ Inter-Service Communication

- **Feign Client** and **RestTemplate** are used for calling other microservices.
- **Interceptors** automatically attach JWT tokens for internal communication.
- **Resilience4j** provides:
  - Circuit Breakers
  - Retry Mechanism
  - Rate Limiting

---

## 📊 Data Flow Example

1. `GET /users/{userId}` request enters through Gateway
2. User-Service fetches user details from MySQL
3. Calls Rating-Service (MongoDB) to get user's ratings
4. For each rating, calls Hotel-Service (PostgreSQL) to fetch hotel info
5. Aggregated response is sent back to client

---

## 🧩 Resilience

- Implemented **Circuit Breaker** to handle service unavailability
- **Retry mechanism** for transient failures
- **Rate Limiter** to prevent overload

---

## 🔍 Monitoring & Discovery

- All services register with **Eureka Discovery Server**
- **Eureka Dashboard** → http://localhost:8761
- Shows live list of registered microservices

---

## 🧰 How to Run

### 1️⃣ Start Config Server
```bash
cd config-server
mvn spring-boot:run
```

### 2️⃣ Start Discovery Server
```bash
cd discovery-server
mvn spring-boot:run
```

### 3️⃣ Start Remaining Services
```bash
mvn spring-boot:run
```
(order: gateway → auth → user → rating → hotel)

### 4️⃣ Access Services

- **Gateway** → http://localhost:8084
- **Eureka Dashboard** → http://localhost:8761
- **Auth Login** → `POST /auth/login`
- **User** → `GET /users/{id}`

---

## 🧠 Concepts Demonstrated

✅ Microservices communication  
✅ JWT Authentication  
✅ API Gateway pattern  
✅ Centralized configuration management  
✅ Circuit breaker / retry pattern  
✅ Service discovery  
✅ Distributed databases  
✅ Rate limiting and fault tolerance  

---

## 🏁 Future Enhancements

- Add centralized logging using **ELK Stack**
- Integrate **Zipkin** or **Sleuth** for distributed tracing
- Containerize all services using **Docker Compose**
- Deploy on **AWS ECS** / **Kubernetes**

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!  
Feel free to check the issues.
---

## 👨‍💻 Author

**Swet Lakhani**  
- GitHub: [@swetlakhani218](https://github.com/swetlakhani218)
- LinkedIn: [Swet Lakhani](www.linkedin.com/in/swet-lakhani-418494262)

---

⭐ **If you found this project helpful, please give it a star!** ⭐
