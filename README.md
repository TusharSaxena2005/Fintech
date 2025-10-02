# Fintech Application

A full-stack fintech application built with Spring Boot (Backend) and React (Frontend) for user account management.

## Features

### Backend (Spring Boot)
- ✅ User CRUD operations (Create, Read, Update, Delete)
- ✅ RESTful API endpoints
- ✅ PostgreSQL database integration
- ✅ ModelMapper for DTO conversion
- ✅ Exception handling
- ✅ CORS configuration
- ✅ JPA/Hibernate for database operations

### Frontend (React)
- ✅ Modern React with Hooks
- ✅ Responsive user interface
- ✅ User management dashboard
- ✅ User creation and editing forms
- ✅ Search and filter functionality
- ✅ Balance management
- ✅ Role-based user display
- ✅ Beautiful UI with gradient design

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- PostgreSQL
- Lombok
- ModelMapper
- Maven

### Frontend
- React 18
- Vite (Build tool)
- Axios (HTTP client)
- Modern CSS with Flexbox/Grid
- ES6+ JavaScript

## API Endpoints

### User Management
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/email/{email}` - Get user by email
- `POST /api/v1/users/signup` - Create new user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user
- `PUT /api/v1/users/{id}/balance?balance={amount}` - Update user balance

## Getting Started

### Prerequisites
- Java 21
- Node.js (20+ recommended, but 18+ should work)
- PostgreSQL database
- Maven

### Backend Setup

1. Navigate to the Backend directory:
   ```bash
   cd Backend
   ```

2. Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-host:port/your-database
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the Frontend directory:
   ```bash
   cd Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend will start on `http://localhost:3000`

## Database Schema

The application uses a single `UserEntity` table:

```sql
CREATE TABLE user_entity (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    card_number VARCHAR(16),
    expiry_date VARCHAR(5),
    cvv VARCHAR(3),
    account_number VARCHAR(20),
    balance DECIMAL(19,2) DEFAULT 0.00
);
```

## User Roles
- `USER` - Regular user
- `ADMIN` - Administrator
- `MANAGER` - Manager

## Project Structure

```
Fintech/
├── Backend/
│   ├── src/main/java/com/fintech/Backend/
│   │   ├── controllers/     # REST controllers
│   │   ├── services/        # Business logic
│   │   ├── repository/      # Data access layer
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data transfer objects
│   │   ├── config/          # Configuration classes
│   │   └── exception/       # Exception handling
│   └── pom.xml
├── Frontend/
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── services/        # API services
│   │   └── assets/          # Static assets
│   └── package.json
└── README.md
```

## Development

### Adding New Features

1. **Backend**: Add endpoints in controllers, implement business logic in services
2. **Frontend**: Create new components and connect to API services

### Code Style
- Follow Java naming conventions for backend
- Use modern React patterns with hooks
- Implement responsive design principles

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is for educational purposes.