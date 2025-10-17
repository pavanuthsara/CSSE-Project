# CSSE Backend - Test Login System

## Overview

This backend system provides authentication and authorization for doctors and staff in a healthcare management system.

## Test Credentials

The system automatically creates test users on startup:

### Doctor Login

- **Username**: `doctor1`
- **Password**: `password123`
- **Role**: DOCTOR
- **Profile**: Dr. John Smith, Cardiology specialist

### Staff Login

- **Username**: `staff1`
- **Password**: `password123`
- **Role**: STAFF
- **Profile**: Jane Doe, Registered Nurse in Emergency

### Admin Login

- **Username**: `admin1`
- **Password**: `password123`
- **Role**: ADMIN
- **Profile**: System administrator

## API Endpoints

### Authentication Endpoints

- `POST /api/auth/login` - Login with credentials
- `POST /api/auth/register` - Register new user

### Test Endpoints (Public Access)

- `GET /api/test/credentials` - Get all test login credentials
- `GET /api/test/system-status` - Check system status
- `POST /api/test/quick-login/doctor` - Quick login as doctor
- `POST /api/test/quick-login/staff` - Quick login as staff
- `POST /api/test/quick-login/admin` - Quick login as admin

### Protected Endpoints

- `GET /api/doctor/dashboard` - Doctor dashboard (requires DOCTOR role)
- `GET /api/doctor/profile` - Doctor profile information
- `GET /api/staff/dashboard` - Staff dashboard (requires STAFF role)
- `GET /api/staff/profile` - Staff profile information
- `GET /api/user/profile` - User profile information

## Testing Instructions

### 1. Start the Application

```bash
./gradlew bootRun
```

### 2. Test System Status

```bash
curl -X GET http://localhost:8080/api/test/system-status
```

### 3. Get Test Credentials

```bash
curl -X GET http://localhost:8080/api/test/credentials
```

### 4. Test Login (Example with Doctor)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"password123"}'
```

### 5. Test Protected Endpoint

```bash
# First login to get token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"password123"}' | jq -r '.token')

# Use token to access protected endpoint
curl -X GET http://localhost:8080/api/doctor/dashboard \
  -H "Authorization: Bearer $TOKEN"
```

### 6. Quick Login Test

```bash
# Quick login as doctor
curl -X POST http://localhost:8080/api/test/quick-login/doctor

# Quick login as staff
curl -X POST http://localhost:8080/api/test/quick-login/staff
```

## Running Tests

### Unit Tests

```bash
./gradlew test
```

### Specific Test Class

```bash
./gradlew test --tests AuthenticationTest
```

## Database Configuration

### Development (PostgreSQL)

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/csse_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Testing (H2 In-Memory)

Tests use H2 database automatically configured in `application-test.properties`.

## Security Features

- JWT-based authentication
- Role-based authorization (DOCTOR, STAFF, ADMIN)
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Comprehensive error handling

## Project Structure

```
src/main/java/com/pavan/csse/backend/
├── config/          # Security configuration
├── controller/      # REST controllers
├── dto/             # Data transfer objects
├── model/           # JPA entities
├── repository/      # Data access layer
├── service/         # Business logic
└── util/            # Utility classes
```

## Troubleshooting

### Common Issues

1. **Database Connection Failed**

   - Check PostgreSQL is running
   - Verify database credentials in `application.properties`

2. **JWT Token Invalid**

   - Check token expiration (24 hours by default)
   - Verify JWT secret configuration

3. **Access Denied**
   - Ensure user has correct role for endpoint
   - Verify token is included in Authorization header

### Logs

- Application logs: Check console output
- Security logs: Enable DEBUG level for `org.springframework.security`
