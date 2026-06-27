# 📦 Inventory Management System

A full-stack web-based Inventory Management System built with **Spring Boot**, **MySQL**, and **Thymeleaf**. It supports product tracking, low-stock email alerts, Excel report exports, user authentication, and Docker-based deployment.


---

## ✨ Features

- 🔐 **User Authentication** — Secure login and access control using Spring Security
- 📦 **Product Management** — Add, update, delete, and view products with full CRUD support
- 📊 **Stock Tracking** — Monitor inventory levels in real time
- 🔔 **Low Stock Alerts** — Automatic email notifications when stock falls below threshold
- 📁 **Excel Reports** — Export inventory data to `.xlsx` files using Apache POI
- 🐳 **Docker Support** — Fully containerized with Docker and Docker Compose

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA + Hibernate |
| Template Engine | Thymeleaf |
| Database | MySQL 8 |
| Email | Spring Boot Mail |
| Reports | Apache POI 5.2.5 |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |
| Utilities | Lombok |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/astha/inventorymanagement/
│   │   ├── controller/       # MVC Controllers
│   │   ├── model/            # Entity classes
│   │   ├── repository/       # JPA Repositories
│   │   ├── service/          # Business logic
│   │   └── InventoryManagementApplication.java
│   └── resources/
│       ├── templates/        # Thymeleaf HTML pages
│       ├── static/           # CSS, JS, images
│       └── application.properties
└── test/
    └── java/                 # Unit tests
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (for containerized setup)
- MySQL 8 (for local setup without Docker)

---

### ▶️ Option 1 — Run with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/astha-1016/inventory-management-system.git
cd inventory-management-system

# 2. Set up environment variables
cp .env.example .env
# Open .env and fill in your values

# 3. Build and start
docker compose up --build
```

Open your browser at **http://localhost:8080**

---

### ▶️ Option 2 — Run Locally (Without Docker)

**1. Set up the database**

Create a MySQL database:
```sql
CREATE DATABASE inventory_management;
```

**2. Configure application.properties**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_management
spring.datasource.username=root
spring.datasource.password=your_password
```

**3. Run the application**

```bash
./mvnw spring-boot:run
```

Open your browser at **http://localhost:8081**

---

## 🔑 Default Login

> Update these credentials in your database or security configuration before deploying.

| Role | Username | Password |
|---|---|---|
| Admin | admin | admin123 |

---

## 📧 Email Configuration

To enable low-stock email alerts, add the following to `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

> Use a Gmail App Password, not your regular password.

---

## 🐳 Docker Configuration

The project includes:
- `Dockerfile` — Multi-stage build (Maven build + JRE runtime)
- `docker-compose.yml` — Spins up the app + MySQL together
- `.env.example` — Template for environment variables

```bash
# Start
docker compose up --build

# Stop
docker compose down

# Stop and remove volumes (clears database)
docker compose down -v
```

---

## 📊 Excel Export

Inventory reports can be exported to `.xlsx` format directly from the UI. Reports are generated using **Apache POI** and include current product names, quantities, and stock status.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feat/your-feature`
5. Open a Pull Request

---

## 👩‍💻 Author

**Astha** — [GitHub](https://github.com/astha-1016)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
