# SmartTaskManager - Architecture Documentation

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Architecture Pattern](#architecture-pattern)
- [Layer Responsibilities](#layer-responsibilities)
- [Package Structure](#package-structure)
- [Domain Model](#domain-model)
- [Data Flow](#data-flow)
- [Configuration Recommendations](#configuration-recommendations)
- [Cross-Cutting Concerns](#cross-cutting-concerns)
- [Integration Points](#integration-points)
- [Scalability Considerations](#scalability-considerations)

---

## Architecture Overview

SmartTaskManager follows a **Layered Architecture (N-Tier)** pattern, which is a standard approach for Spring Boot applications. This pattern provides clear separation of concerns and makes the codebase maintainable and testable.

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  ┌───────────────────────────────────────────────────┐  │
│  │         REST Controllers (controller/)            │  │
│  │  - Handle HTTP requests/responses                 │  │
│  │  - Request validation                             │  │
│  │  - Response mapping                               │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    Business Layer                        │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Services (service/)                       │  │
│  │  - Business logic                                 │  │
│  │  - Transaction management                         │  │
│  │  - Domain orchestration                           │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    Data Access Layer                     │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Repositories (repository/)                │  │
│  │  - JPA repositories                               │  │
│  │  - Custom queries                                 │  │
│  │  - Data persistence                               │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    Domain Layer                          │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Entities (domain/entity/)                  │  │
│  │  - JPA entities                                   │  │
│  │  - Domain models                                  │  │
│  │  - Business rules                                 │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │         DTOs (dto/)                               │  │
│  │  - Request DTOs                                   │  │
│  │  - Response DTOs                                  │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## Architecture Pattern

### Layered Architecture

**Benefits:**
- Clear separation of concerns
- Easy to understand and maintain
- Testable components
- Standard Spring Boot pattern

**Layers:**
1. **Presentation Layer** - Controllers handle HTTP
2. **Business Layer** - Services contain business logic
3. **Data Access Layer** - Repositories handle persistence
4. **Domain Layer** - Entities and DTOs represent domain

**Communication Flow:**
- Controllers → Services → Repositories → Database
- DTOs used for external communication
- Entities used internally for persistence

---

## Layer Responsibilities

### Controller Layer (`controller/`)

**Current Status:** Empty

**Responsibilities:**
- Handle HTTP requests (GET, POST, PUT, DELETE)
- Validate request parameters and body
- Call service layer methods
- Map responses to DTOs
- Handle HTTP status codes
- Exception handling (delegated to global handler)

**Example Structure (Recommended):**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        // Implementation
    }
}
```

### Service Layer (`service/`)

**Current Status:** Empty

**Responsibilities:**
- Implement business logic
- Manage transactions (`@Transactional`)
- Orchestrate domain operations
- Validate business rules
- Handle domain events
- Coordinate between repositories

**Example Structure (Recommended):**
```java
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserDTO createUser(CreateUserDTO dto) {
        // Business logic
        // Validation
        // Persistence
    }
}
```

### Repository Layer (`repository/`)

**Current Status:** Empty

**Responsibilities:**
- Data persistence operations
- Custom queries (JPQL, native SQL)
- Query optimization
- Data access abstraction

**Example Structure (Recommended):**
```java
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
}
```

### Domain Layer

#### Entities (`domain/entity/`)

**Current Status:** Partially implemented

**Responsibilities:**
- Represent database tables
- Define relationships
- Enforce domain constraints
- Contain domain logic

**Entities:**
- `User` - Partially implemented (has bugs)
- `Project` - Empty
- `Task` - Empty

#### DTOs (`dto/`)

**Current Status:** Empty

**Responsibilities:**
- Transfer data between layers
- Separate internal entities from external API
- Validation annotations
- Request/Response models

**Recommended Structure:**
- `UserRequestDTO` - For creating/updating users
- `UserResponseDTO` - For returning user data
- Similar DTOs for Project and Task

---

## Package Structure

### Current Package Organization

```
com.smarttask/
├── config/          # Configuration classes
│   └── (empty)
├── controller/      # REST controllers
│   └── (empty)
├── domain/
│   └── entity/      # JPA entities
│       ├── User.java      (partial)
│       ├── Project.java   (empty)
│       └── Task.java      (empty)
├── dto/             # Data Transfer Objects
│   └── (empty)
├── event/           # Domain events
│   └── (empty)
├── repository/      # JPA repositories
│   └── (empty)
├── service/         # Business services
│   └── (empty)
├── util/            # Utility classes
│   └── (empty)
└── SmartTaskManagerApplication.java
```

### Recommended Package Structure

For future growth, consider organizing by feature:

```
com.smarttask/
├── user/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── domain/
├── project/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── domain/
└── task/
    ├── controller/
    ├── service/
    ├── repository/
    ├── dto/
    └── domain/
```

**Note:** Current structure is acceptable for small-to-medium projects. Feature-based structure recommended for larger applications.

---

## Domain Model

### Entity Relationships

```
┌──────────┐         ┌──────────┐
│   User   │         │  Project │
└────┬─────┘         └────┬─────┘
     │                    │
     │ 1                  │ 1
     │                    │
     │                    │
     └──────────┬─────────┘
                │
                │ N
                │
           ┌────▼────┐
           │  Task   │
           └─────────┘
```

### Entity Details

#### User Entity
- **Primary Key:** UUID
- **Fields:** id, name, email
- **Relationships:** One-to-Many with Task
- **Status:** Partially implemented (needs fixes)

#### Project Entity
- **Status:** Not implemented
- **Expected Fields:** id, name, description, createdAt, updatedAt
- **Relationships:** One-to-Many with Task

#### Task Entity
- **Status:** Not implemented
- **Expected Fields:** id, title, description, status, priority, dueDate
- **Relationships:** Many-to-One with User, Many-to-One with Project

---

## Data Flow

### Request Flow

```
HTTP Request
    │
    ▼
Controller (Validates request, maps to DTO)
    │
    ▼
Service (Business logic, validation)
    │
    ▼
Repository (Data access)
    │
    ▼
Database (PostgreSQL)
```

### Response Flow

```
Database (PostgreSQL)
    │
    ▼
Repository (Entity)
    │
    ▼
Service (Business logic, Entity → DTO)
    │
    ▼
Controller (DTO → HTTP Response)
    │
    ▼
HTTP Response
```

### DTO Mapping Strategy

**Recommended Approach:**
- Use MapStruct for automatic mapping (optional)
- Manual mapping in service layer (current approach)
- Separate request/response DTOs

**Mapping Flow:**
```
RequestDTO → Entity (for persistence)
Entity → ResponseDTO (for API response)
```

---

## Configuration Recommendations

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smarttask
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
```

### JPA Configuration

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Use 'update' in dev, 'validate' in prod
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        use_sql_comments: true
    open-in-view: false  # Best practice
```

### Logging Configuration

```yaml
logging:
  level:
    root: INFO
    com.smarttask: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### Actuator Configuration

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

### Profile Configuration

```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  config:
    activate:
      on-profile: dev,test,prod
```

---

## Cross-Cutting Concerns

### Current Status

| Concern | Status | Implementation |
|---------|--------|----------------|
| **Logging** | ❌ Not configured | No structured logging |
| **Exception Handling** | ❌ Not implemented | No global exception handler |
| **Validation** | ❌ Not configured | No Bean Validation |
| **Security** | ❌ Not configured | No Spring Security |
| **Transaction Management** | ⚠️ Not used | No `@Transactional` |
| **Caching** | ❌ Not configured | No cache strategy |

### Recommended Implementations

#### 1. Exception Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        // Handle 404
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        // Handle 400
    }
}
```

#### 2. Validation

- Add `spring-boot-starter-validation` dependency
- Use `@Valid` in controllers
- Add validation annotations to DTOs (`@NotNull`, `@Email`, `@Size`, etc.)

#### 3. Security

- Add `spring-boot-starter-security` dependency
- Configure authentication (JWT recommended)
- Define authorization rules
- Set up CORS if needed

#### 4. Transaction Management

- Use `@Transactional` on service methods
- Configure transaction isolation levels if needed
- Handle rollback scenarios

---

## Integration Points

### Current Integrations

- **PostgreSQL Database** - Configured via JPA
- **Spring Actuator** - Health checks and metrics

### Potential Future Integrations

- **Message Queue** (Kafka/RabbitMQ) - For async processing
- **Cache** (Redis) - For performance optimization
- **External APIs** - Via Feign Client or WebClient
- **Email Service** - For notifications
- **File Storage** - For attachments

---

## Scalability Considerations

### Current Architecture Limitations

1. **Monolithic Structure** - All features in single application
2. **Single Database** - All entities in one database
3. **No Caching** - Every request hits database
4. **Synchronous Processing** - No async capabilities

### Scalability Options

#### Short-term (Current Architecture)
- Add caching layer (Redis)
- Optimize database queries
- Implement pagination
- Add connection pooling

#### Medium-term
- Implement async processing
- Add message queue for heavy operations
- Optimize JPA queries and relationships

#### Long-term
- Consider microservices architecture
- Separate read/write databases (CQRS)
- Implement event-driven architecture
- Add API gateway

---

---
Generated on: 2025-01-27 12:00:00
