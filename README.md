# Rexmart — Scalable Full-Stack E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-24-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

**Rexmart** is an enterprise-ready, high-concurrency full-stack e-commerce web platform engineered with **Java / Spring Boot** and a responsive **HTML5/CSS3/ES6+ JavaScript** frontend. 

It provides an end-to-end e-commerce experience featuring a **Flipkart-style guest browsing experience** for shoppers, **stateless JWT security**, **dual-mode distributed caching**, and an administrative dashboard for inventory and order management.

---

## 🌟 Key Features

* **🛍️ Guest Browsing & Smart Auth Gate:** Shoppers can freely browse products, view categories, and filter listings without logging in. Adding items to Cart or Wishlist triggers a modal prompt redirecting to secure login.
* **🔐 Stateless JWT Authentication:** Powered by Spring Security and JSON Web Tokens (JJWT 0.12.x). Passwords are securely encrypted using salted **BCrypt**.
* **⚡ High-Performance Dual-Mode Caching:** Catalog read queries are cached via Spring Cache. By default, it operates with local thread-safe JVM in-memory caching (`ConcurrentMapCacheManager`); in production, it connects to a distributed **Redis** instance for sub-5ms response times.
* **🔄 Automated Cache Eviction:** Product modifications, additions, and deletions trigger `@CacheEvict` to prevent stale data.
* **🏊 Database Concurrency Tuning:** Powered by **HikariCP** connection pooling tuned to 50 active channels, eliminating SQL socket starvation under traffic surges.
* **📦 Microservices Orchestration:** Fully containerized with a multi-container `docker-compose.yml` orchestrating the Spring Boot application, PostgreSQL, and Redis cache on an isolated bridge network.
* **👑 Administrative Control Portal:** Comprehensive dashboard with revenue telemetry, catalog inventory management (CRUD), customer account audits, and real-time order lifecycle tracking (`PENDING` ➔ `CONFIRMED` ➔ `SHIPPED` ➔ `DELIVERED`).

---

## 🏗️ System Architecture

```mermaid
graph TD
    Client["Client Browser (HTML5 / Vanilla JS)"]
    Interceptor["Global Fetch Interceptor (Bearer Token)"]
    SecurityFilter["Spring Security Filter Chain (JwtFilter)"]
    Controller["REST Controllers (/api/*)"]
    Service["Service Layer (Business Logic)"]
    Cache["Dual-Mode Cache (Redis / JVM ConcurrentHashMap)"]
    JPA["Spring Data JPA (Hibernate 6)"]
    DB[("PostgreSQL Database")]

    Client -->|HTTP Requests| Interceptor
    Interceptor -->|Authorization: Bearer <token>| SecurityFilter
    SecurityFilter -->|Stateless SecurityContext| Controller
    Controller -->|DTO Validation| Service
    Service <-->|Cache-Aside @Cacheable / @CacheEvict| Cache
    Service -->|Entities & Mappings| JPA
    JPA <-->|HikariCP Connection Pool (Max: 50)| DB
```

---

## 🛠️ Technology Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Backend** | Java 24 / 21 LTS, Spring Boot 3.5.x | REST API server, IoC & Dependency Injection |
| **Security** | Spring Security 6, JJWT 0.12.6, BCrypt | Stateless authorization filter chain, token validation |
| **Persistence** | Spring Data JPA, Hibernate 6.6 | Object-Relational Mapping (ORM) and DTO projection |
| **Database** | PostgreSQL 15 | Relational ACID persistence |
| **Connection Pool** | HikariCP | High-throughput concurrent connection pooling |
| **Caching** | Redis 7 & Spring Cache Abstraction | Distributed low-latency key-value cache |
| **Frontend** | HTML5, Modern CSS3, JavaScript ES6+, Fetch API | Glassmorphic UI templates, AJAX request routing |
| **Documentation** | SpringDoc OpenAPI (Swagger UI) | Live interactive API contract sandbox |
| **DevOps** | Docker, Docker Compose | Multi-container reproducible deployment |
| **Build & Tools** | Maven 3.9, Git, GitHub Desktop | Package lifecycle management and version control |

---

## 🗄️ Database Entity Schema (10 Entities)

* **`Customer`**: Core profile storing customer metadata, linked 1:1 with `User`, `Cart`, and `Wishlist`, and 1:N with `Address`, `Order`, and `Review`.
* **`User`**: Security credentials entity holding email, role (`CUSTOMER` / `ADMIN`), and BCrypt password.
* **`Product`**: Master catalog holding title, pricing, stock levels, category, and image URL.
* **`Cart` & `CartItem`**: One-to-one shopping cart lifecycle with orphan removal upon order checkout.
* **`Order` & `OrderStatus`**: Completed customer transaction record linked M:N with `Product` via `order_items`.
* **`Address`**: Shipping and billing address destinations.
* **`Wishlist`**: Customer saved items list via `wishlist_items`.
* **`Review`**: Customer rating (1–5 stars) and feedback comments on products.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK 21 or 24)**
* **PostgreSQL** running on port `5432` (or Docker)
* **Maven** (bundled via `./mvnw`)

### 1. Local Run (Without Docker)
```bash
# Clone the repository
git clone https://github.com/tejasrepale33/Rexmart-Ecommerce-website.git
cd Rexmart-Ecommerce-website

# Build and package
./mvnw clean package -DskipTests

# Run the Spring Boot application
./mvnw spring-boot:run
```
* Customer Store: [http://localhost:8080/customer-home.html](http://localhost:8080/customer-home.html)
* Admin Dashboard: [http://localhost:8080/admin-login.html](http://localhost:8080/admin-login.html)
* Swagger UI Docs: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 2. Multi-Container Run (Docker Compose)
To spin up the App, PostgreSQL, and Redis cache automatically in one command:
```bash
docker-compose up --build -d
```

---

## 👤 Author
**Tejas Repale**  
* GitHub: [@tejasrepale33](https://github.com/tejasrepale33)
* LinkedIn: [Tejas Repale](https://linkedin.com/in/tejas-repale-3239543a5)
