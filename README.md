# Task Manager Application

A full-stack task management application built with Spring Boot (backend) and React TypeScript (frontend). This application provides a clean, modern interface for managing tasks with full CRUD operations.

## 🚀 Features

- **Create, Read, Update, Delete** tasks
- **Task Status Management**: TODO, IN_PROGRESS, DONE
- **Search and Filter** tasks by status and title
- **Real-time Statistics** dashboard
- **Responsive Design** for mobile and desktop
- **Input Validation** and error handling
- **RESTful API** with proper HTTP status codes
- **Containerized** with Docker

## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Frontend (React TypeScript)
- **Framework**: React 18 with TypeScript
- **HTTP Client**: Axios
- **Styling**: CSS3 with modern design
- **Build Tool**: Create React App

### Persistence Layer Choice

**H2 In-Memory Database** was chosen for this implementation because:

1. **Simplicity**: No external database setup required
2. **Development Speed**: Quick to get started and test
3. **Portability**: Runs anywhere Java runs
4. **Testing**: Perfect for unit and integration tests
5. **Demo Purpose**: Ideal for showcasing the application

For production use, this can easily be switched to PostgreSQL, MySQL, or any other JPA-supported database by changing the configuration.

## 📋 API Endpoints

### Tasks API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/tasks` | Get all tasks |
| GET | `/tasks?status={status}` | Get tasks by status |
| GET | `/tasks?search={term}` | Search tasks by title |
| GET | `/tasks/{id}` | Get task by ID |
| POST | `/tasks` | Create new task |
| PUT | `/tasks/{id}` | Update task |
| DELETE | `/tasks/{id}` | Delete task |

### Task Object Structure

```json
{
  "id": "uuid",
  "title": "string (required)",
  "description": "string (optional)",
  "status": "TODO | IN_PROGRESS | DONE"
}
```

## 🛠️ Setup and Installation

### Prerequisites

- Docker and Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for local development)
- Maven 3.6+ (for local development)

### Option 1: Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone git@github.com:jamescdudley/task-manager.git
   cd task-manager
   ```

2. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

### Option 2: Local Development

#### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or if Maven is installed globally:
   ```bash
   mvn spring-boot:run
   ```

3. **Backend will be available at**: http://localhost:8080

#### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Frontend will be available at**: http://localhost:3000

## 🧪 Testing

### Backend Tests

```bash
cd backend
./mvnw test
```

The backend includes:
- **Unit Tests**: Service layer with Mockito
- **Integration Tests**: Controller layer with MockMvc
- **Application Context Tests**: Ensuring proper Spring Boot configuration

### Frontend Tests

```bash
cd frontend
npm test
```

## 🐳 Docker

### Individual Container Builds

#### Backend
```bash
cd backend
docker build -t task-manager-backend .
docker run -p 8080:8080 task-manager-backend
```

#### Frontend
```bash
cd frontend
docker build -t task-manager-frontend .
docker run -p 3000:80 task-manager-frontend
```

## 📱 Usage

1. **Access the application** at http://localhost:3000
2. **Add a new task** by clicking the "➕ Add New Task" button
3. **View task statistics** in the dashboard cards
4. **Filter tasks** by status using the dropdown
5. **Search tasks** by title using the search bar
6. **Mark tasks as done** using the "✓ Done" button
7. **Edit tasks** using the "✏️ Edit" button
8. **Delete tasks** using the "🗑️ Delete" button

## 🔧 Configuration

### Backend Configuration

The application can be configured via `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

### Frontend Configuration

Environment variables can be set in `.env` file:

```env
REACT_APP_API_URL=http://localhost:8080
```

## 🚀 Production Deployment

For production deployment:

1. **Update database configuration** to use a persistent database
2. **Set proper CORS origins** in the backend
3. **Configure environment variables** for different environments
4. **Use production-ready web server** (already configured with nginx in Docker)
5. **Implement proper logging and monitoring**

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🔍 Troubleshooting

### Common Issues

1. **Backend not starting**: Check if port 8080 is available
2. **Frontend can't connect to backend**: Verify REACT_APP_API_URL is correct
3. **Docker build fails**: Ensure Docker has enough memory allocated
4. **Tests failing**: Check Java version (requires Java 17+)

### Health Checks

- Backend health: http://localhost:8080/tasks
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`)

## 📊 Performance Considerations

- **Database**: H2 is suitable for development; use PostgreSQL/MySQL for production
- **Caching**: Consider adding Redis for session management in production
- **API Rate Limiting**: Implement rate limiting for production APIs
- **Frontend Optimization**: The build process includes minification and compression

## 🔐 Security Features

- **Input Validation**: Server-side validation using Bean Validation
- **CORS Configuration**: Configurable cross-origin resource sharing
- **SQL Injection Prevention**: JPA/Hibernate prevents SQL injection
- **XSS Protection**: React's built-in XSS protection
- **Security Headers**: Nginx configuration includes security headers 