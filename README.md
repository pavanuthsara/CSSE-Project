# 🏥 Healthcare Management System (CSSE Project)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.1.1-blue.svg)](https://reactjs.org)
[![Vite](https://img.shields.io/badge/Vite-7.1.7-646CFF.svg)](https://vitejs.dev)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org)

> A comprehensive healthcare management system built with Spring Boot backend and React frontend, featuring role-based authentication for doctors, staff, and administrators.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This Healthcare Management System is designed to streamline healthcare operations by providing secure, role-based access for different types of users including doctors, medical staff, and administrators. The system features a robust authentication mechanism, comprehensive user management, and scalable architecture.

## ✨ Features

### 🔐 Authentication & Authorization

- JWT-based authentication
- Role-based access control (DOCTOR, STAFF, ADMIN)
- Secure password encryption (BCrypt)
- Session management

### 👥 User Management

- Doctor profile management with specializations
- Staff profile management with departments
- Admin dashboard for system oversight
- User registration and profile updates

### 🏗️ System Features

- RESTful API design
- CORS-enabled for frontend integration
- Comprehensive error handling
- Automated test data initialization
- Real-time system status monitoring

### 🧪 Development & Testing

- Automated test user creation
- Unit and integration tests
- In-memory H2 database for testing
- Quick-login endpoints for development

## 🏛️ Architecture

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│                 │◄──────────────► │                 │
│   React Client  │                 │  Spring Boot    │
│   (Frontend)    │                 │   (Backend)     │
│                 │                 │                 │
└─────────────────┘                 └─────────┬───────┘
                                              │
                                              │ JPA/Hibernate
                                              ▼
                                    ┌─────────────────┐
                                    │   PostgreSQL    │
                                    │    Database     │
                                    └─────────────────┘
```

## 🛠️ Technology Stack

### Backend

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL (Production), H2 (Testing)
- **ORM**: JPA/Hibernate
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockMvc

### Frontend

- **Framework**: React 19.1.1
- **Build Tool**: Vite 7.1.7
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Language**: JavaScript/JSX

### Development Tools

- **IDE**: VS Code / IntelliJ IDEA
- **Version Control**: Git
- **API Testing**: Postman / curl
- **Database Tools**: pgAdmin / DBeaver

## 📁 Project Structure

```
repo-v2/
├── backend/                    # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pavan/csse/backend/
│   │   │   │   ├── config/     # Security & Configuration
│   │   │   │   ├── controller/ # REST Controllers
│   │   │   │   ├── dto/        # Data Transfer Objects
│   │   │   │   ├── model/      # JPA Entities
│   │   │   │   ├── repository/ # Data Access Layer
│   │   │   │   ├── service/    # Business Logic
│   │   │   │   └── util/       # Utility Classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/               # Test Classes
│   ├── build.gradle           # Build Configuration
│   └── TEST_LOGIN_GUIDE.md    # Testing Documentation
├── frontend/                   # React Frontend
│   ├── src/
│   │   ├── components/        # React Components
│   │   ├── assets/           # Static Assets
│   │   ├── App.jsx           # Main App Component
│   │   └── main.jsx          # Entry Point
│   ├── package.json          # Dependencies
│   └── vite.config.js        # Build Configuration
└── README.md                  # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 12** or higher
- **Git**

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/pavanuthsara/CSSE-Project.git
   cd CSSE-Project
   ```

2. **Setup Backend**

   ```bash
   cd backend

   # Configure database in application.properties
   # Update the following properties:
   # spring.datasource.url=jdbc:postgresql://localhost:5432/csse_db
   # spring.datasource.username=your_username
   # spring.datasource.password=your_password

   # Run the application
   ./gradlew bootRun
   ```

3. **Setup Frontend**

   ```bash
   cd ../frontend

   # Install dependencies
   npm install

   # Start development server
   npm run dev
   ```

4. **Access the Application**
   - Backend API: http://localhost:8080
   - Frontend: http://localhost:5173
   - API Documentation: http://localhost:8080/api/test/system-status

### 🔑 Test Credentials

The system automatically creates test users on startup:

| Role       | Username  | Password      | Description                |
| ---------- | --------- | ------------- | -------------------------- |
| **Doctor** | `doctor1` | `password123` | Dr. John Smith, Cardiology |
| **Staff**  | `staff1`  | `password123` | Jane Doe, Emergency Nurse  |
| **Admin**  | `admin1`  | `password123` | System Administrator       |

## 📚 API Documentation

### Base URL

```
http://localhost:8080/api
```

### Authentication Endpoints

```http
POST /auth/login          # User login
POST /auth/register       # User registration
```

### Test Endpoints (Development)

```http
GET  /test/credentials    # Get test login credentials
GET  /test/system-status  # System health check
POST /test/quick-login/doctor  # Quick doctor login
POST /test/quick-login/staff   # Quick staff login
```

### Protected Endpoints

```http
GET /doctor/dashboard     # Doctor dashboard (DOCTOR role)
GET /doctor/profile       # Doctor profile
GET /staff/dashboard      # Staff dashboard (STAFF role)
GET /staff/profile        # Staff profile
GET /user/profile         # User profile (Any authenticated user)
```

### Example Usage

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"password123"}'

# Access protected endpoint
curl -X GET http://localhost:8080/api/doctor/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🧪 Testing

### Backend Tests

```bash
cd backend

# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests AuthenticationTest

# Generate test report
./gradlew test jacocoTestReport
```

### Frontend Tests

```bash
cd frontend

# Run tests (when implemented)
npm test

# Run linting
npm run lint
```

### Manual Testing

```bash
# Check system status
curl http://localhost:8080/api/test/system-status

# Get test credentials
curl http://localhost:8080/api/test/credentials

# Quick login test
curl -X POST http://localhost:8080/api/test/quick-login/doctor
```

## 🚀 Deployment

### Backend Deployment

1. **Build the application**

   ```bash
   ./gradlew build
   ```

2. **Run the JAR file**
   ```bash
   java -jar build/libs/backend-0.0.1-SNAPSHOT.jar
   ```

### Frontend Deployment

1. **Build for production**

   ```bash
   npm run build
   ```

2. **Serve the built files**
   ```bash
   npm run preview
   ```

### Environment Configuration

- **Development**: Uses application.properties
- **Production**: Use environment variables or application-prod.properties
- **Testing**: Uses application-test.properties with H2 database

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Development Guidelines

- Follow Spring Boot best practices for backend
- Use React hooks and functional components for frontend
- Write unit tests for new features
- Maintain consistent code formatting
- Update documentation for API changes

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- **Project Repository**: [CSSE-Project](https://github.com/pavanuthsara/CSSE-Project)
- **Issues**: [GitHub Issues](https://github.com/pavanuthsara/CSSE-Project/issues)
- **Documentation**: [Backend Testing Guide](backend/TEST_LOGIN_GUIDE.md)

## 🏆 Acknowledgments

- Built as part of CSSE (Computer Systems & Software Engineering) coursework
- Spring Boot team for excellent framework documentation
- React team for the amazing frontend library
- Contributors and reviewers

---

**Made with ❤️ for Healthcare Management**
