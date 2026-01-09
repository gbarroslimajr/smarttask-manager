# SmartTaskManager - Configuration Guide

## Table of Contents

- [Overview](#overview)
- [Application Configuration](#application-configuration)
- [Database Configuration](#database-configuration)
- [JPA/Hibernate Configuration](#jpahibernate-configuration)
- [Logging Configuration](#logging-configuration)
- [Actuator Configuration](#actuator-configuration)
- [Profile Configuration](#profile-configuration)
- [Environment Variables](#environment-variables)
- [Security Configuration](#security-configuration)
- [Validation Configuration](#validation-configuration)
- [Complete Configuration Example](#complete-configuration-example)
- [Troubleshooting](#troubleshooting)

---

## Overview

This guide provides comprehensive configuration recommendations for SmartTaskManager. The current `application.yml` contains only the application name. This document outlines all necessary configurations for a production-ready application.

**Current Configuration Status:**
- ✅ Application name configured
- ❌ Database connection missing
- ❌ JPA/Hibernate settings missing
- ❌ Logging configuration missing
- ❌ Actuator endpoints missing
- ❌ Profile management missing

---

## Application Configuration

### Basic Application Settings

```yaml
spring:
  application:
    name: SmartTaskManager
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
```

**Explanation:**
- `spring.application.name`: Used by Spring Cloud, Actuator, and logging
- `spring.profiles.active`: Activates specific profile (dev, test, prod)

---

## Database Configuration

### PostgreSQL DataSource Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:smarttask}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: ${DB_POOL_MAX:10}
      minimum-idle: ${DB_POOL_MIN:5}
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
```

**Configuration Details:**

| Property | Default | Description |
|----------|---------|-------------|
| `url` | `jdbc:postgresql://localhost:5432/smarttask` | Database connection URL |
| `username` | `postgres` | Database username |
| `password` | `postgres` | Database password |
| `maximum-pool-size` | `10` | Maximum connection pool size |
| `minimum-idle` | `5` | Minimum idle connections |
| `connection-timeout` | `30000` | Connection timeout in milliseconds |
| `idle-timeout` | `600000` | Idle connection timeout (10 minutes) |
| `max-lifetime` | `1800000` | Maximum connection lifetime (30 minutes) |
| `leak-detection-threshold` | `60000` | Connection leak detection threshold |

**Environment Variables:**
- `DB_HOST` - Database host (default: localhost)
- `DB_PORT` - Database port (default: 5432)
- `DB_NAME` - Database name (default: smarttask)
- `DB_USERNAME` - Database username (default: postgres)
- `DB_PASSWORD` - Database password (default: postgres)
- `DB_POOL_MAX` - Maximum pool size (default: 10)
- `DB_POOL_MIN` - Minimum pool size (default: 5)

---

## JPA/Hibernate Configuration

### Basic JPA Settings

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: ${JPA_DDL_AUTO:validate}
    show-sql: ${JPA_SHOW_SQL:false}
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        use_sql_comments: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
    open-in-view: false
```

### Configuration Details

#### `ddl-auto` Options

| Value | Description | Usage |
|-------|-------------|-------|
| `none` | No schema management | Production |
| `validate` | Validate schema, no changes | Production |
| `update` | Update schema if needed | Development |
| `create` | Drop and create schema | Development/Testing |
| `create-drop` | Create on startup, drop on shutdown | Testing |

**Recommendation:**
- **Development:** `update` or `validate`
- **Production:** `validate` or `none` (use Flyway/Liquibase)

#### Other Properties

| Property | Default | Description |
|----------|---------|-------------|
| `show-sql` | `false` | Log SQL statements (set to `true` in dev) |
| `format_sql` | `true` | Format SQL in logs |
| `use_sql_comments` | `true` | Add comments to SQL |
| `batch_size` | `20` | Batch size for inserts/updates |
| `order_inserts` | `true` | Order inserts for batching |
| `order_updates` | `true` | Order updates for batching |
| `open-in-view` | `false` | **Best practice:** Keep false to avoid lazy loading issues |

### Advanced JPA Configuration

```yaml
spring:
  jpa:
    properties:
      hibernate:
        # Performance tuning
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true

        # Query cache (if using second-level cache)
        cache:
          use_second_level_cache: false
          use_query_cache: false

        # Statistics (for monitoring)
        generate_statistics: false

        # Naming strategy
        physical_naming_strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
```

---

## Logging Configuration

### Basic Logging Setup

```yaml
logging:
  level:
    root: INFO
    com.smarttask: ${LOG_LEVEL:DEBUG}
    org.springframework.web: INFO
    org.springframework.security: DEBUG
    org.hibernate.SQL: ${HIBERNATE_SQL_LOG:DEBUG}
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/smarttask-manager.log
    max-size: 10MB
    max-history: 30
```

### Logging Levels

| Level | Usage | Description |
|-------|-------|-------------|
| `TRACE` | Development | Most verbose, all details |
| `DEBUG` | Development | Debug information |
| `INFO` | Production | General information |
| `WARN` | Production | Warning messages |
| `ERROR` | Production | Error messages only |

### Recommended Log Levels by Environment

**Development:**
```yaml
logging:
  level:
    com.smarttask: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

**Production:**
```yaml
logging:
  level:
    com.smarttask: INFO
    org.hibernate.SQL: WARN
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
```

### Log File Configuration

```yaml
logging:
  file:
    name: logs/smarttask-manager.log
    max-size: 10MB
    max-history: 30
    total-size-cap: 1GB
```

**Properties:**
- `name`: Log file path
- `max-size`: Maximum size per log file before rotation
- `max-history`: Number of archived log files to keep
- `total-size-cap`: Total size of all log files

---

## Actuator Configuration

### Basic Actuator Setup

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
      probes:
        enabled: true
    info:
      enabled: true
    metrics:
      enabled: true
  health:
    db:
      enabled: true
```

### Available Endpoints

| Endpoint | Description | Security |
|----------|-------------|----------|
| `/actuator/health` | Application health status | Public (recommended) |
| `/actuator/info` | Application information | Public |
| `/actuator/metrics` | Application metrics | Protected |
| `/actuator/env` | Environment variables | Protected |
| `/actuator/loggers` | Logger configuration | Protected |

### Security Recommendations

**Development:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"  # Expose all endpoints
```

**Production:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: never  # Hide details in production
```

---

## Profile Configuration

### Profile-Specific Configuration

Create separate configuration files for each environment:

**application-dev.yml:**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/smarttask_dev

logging:
  level:
    com.smarttask: DEBUG
```

**application-test.yml:**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/smarttask_test

logging:
  level:
    com.smarttask: DEBUG
```

**application-prod.yml:**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10

logging:
  level:
    com.smarttask: INFO
    root: WARN

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: never
```

### Activating Profiles

**Via Environment Variable:**
```bash
export SPRING_PROFILES_ACTIVE=prod
```

**Via Application Property:**
```yaml
spring:
  profiles:
    active: prod
```

**Via Command Line:**
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## Environment Variables

### Required Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_HOST` | Database host | `localhost` | No |
| `DB_PORT` | Database port | `5432` | No |
| `DB_NAME` | Database name | `smarttask` | No |
| `DB_USERNAME` | Database username | `postgres` | No |
| `DB_PASSWORD` | Database password | `postgres` | **Yes (Production)** |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` | No |
| `LOG_LEVEL` | Application log level | `DEBUG` | No |

### Setting Environment Variables

**Linux/macOS:**
```bash
export DB_PASSWORD=mysecretpassword
export SPRING_PROFILES_ACTIVE=prod
```

**Windows (Command Prompt):**
```cmd
set DB_PASSWORD=mysecretpassword
set SPRING_PROFILES_ACTIVE=prod
```

**Windows (PowerShell):**
```powershell
$env:DB_PASSWORD="mysecretpassword"
$env:SPRING_PROFILES_ACTIVE="prod"
```

**Docker:**
```yaml
environment:
  - DB_PASSWORD=mysecretpassword
  - SPRING_PROFILES_ACTIVE=prod
```

---

## Security Configuration

### Basic Security Settings

```yaml
spring:
  security:
    user:
      name: admin
      password: ${ADMIN_PASSWORD:changeme}
      roles: ADMIN
```

**Note:** This is a basic configuration. For production, implement proper Spring Security with JWT or OAuth2.

### CORS Configuration

```yaml
spring:
  web:
    cors:
      allowed-origins: ${CORS_ORIGINS:http://localhost:3000,http://localhost:8080}
      allowed-methods: GET,POST,PUT,DELETE,OPTIONS
      allowed-headers: "*"
      allow-credentials: true
      max-age: 3600
```

---

## Validation Configuration

### Bean Validation

Add dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

No additional configuration needed in `application.yml`. Validation is enabled by default when the dependency is present.

### Custom Validation Messages

Create `ValidationMessages.properties` in `src/main/resources`:

```properties
user.email.invalid=Email address is invalid
user.name.required=User name is required
task.title.required=Task title is required
```

---

## Complete Configuration Example

### Complete `application.yml`

```yaml
spring:
  application:
    name: SmartTaskManager

  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}

  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:smarttask}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: ${DB_POOL_MAX:10}
      minimum-idle: ${DB_POOL_MIN:5}
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

  jpa:
    hibernate:
      ddl-auto: ${JPA_DDL_AUTO:validate}
    show-sql: ${JPA_SHOW_SQL:false}
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        use_sql_comments: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
    open-in-view: false

  web:
    cors:
      allowed-origins: ${CORS_ORIGINS:http://localhost:3000}
      allowed-methods: GET,POST,PUT,DELETE,OPTIONS
      allowed-headers: "*"
      allow-credentials: true

logging:
  level:
    root: INFO
    com.smarttask: ${LOG_LEVEL:DEBUG}
    org.springframework.web: INFO
    org.hibernate.SQL: ${HIBERNATE_SQL_LOG:DEBUG}
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/smarttask-manager.log
    max-size: 10MB
    max-history: 30

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
      probes:
        enabled: true
  health:
    db:
      enabled: true

server:
  port: ${SERVER_PORT:8080}
  error:
    include-message: always
    include-binding-errors: always
    include-stacktrace: on_param
    include-exception: false
```

---

## Troubleshooting

### Common Configuration Issues

#### 1. Database Connection Failed

**Error:**
```
Unable to acquire JDBC Connection
```

**Solutions:**
- Verify PostgreSQL is running
- Check database credentials
- Verify database exists
- Check network connectivity
- Review connection pool settings

#### 2. Schema Validation Failed

**Error:**
```
Schema-validation: wrong column type encountered
```

**Solutions:**
- Set `ddl-auto: update` to auto-update schema
- Or manually update database schema
- Or use Flyway/Liquibase for schema management

#### 3. Actuator Endpoints Not Accessible

**Error:**
```
404 Not Found on /actuator/health
```

**Solutions:**
- Verify `management.endpoints.web.exposure.include` includes desired endpoints
- Check base path configuration
- Verify security configuration allows access

#### 4. Logging Not Working

**Error:**
- No log files created
- Logs not appearing

**Solutions:**
- Verify log file path is writable
- Check log level configuration
- Verify logging dependencies are present

#### 5. Profile Not Activated

**Error:**
- Wrong configuration loaded

**Solutions:**
- Verify `SPRING_PROFILES_ACTIVE` environment variable
- Check profile-specific YAML files exist
- Verify profile name matches file name

---

## Configuration Best Practices

1. **Use Environment Variables** for sensitive data (passwords, API keys)
2. **Profile-Specific Configs** for different environments
3. **Externalize Configuration** - Don't hardcode values
4. **Validate Configuration** on application startup
5. **Document All Configuration** options
6. **Use Property Placeholders** for flexibility
7. **Keep Secrets Secure** - Never commit passwords to version control
8. **Monitor Configuration** via Actuator endpoints

---

## Next Steps

After configuring the application:

1. Review [Architecture Documentation](./architecture.md) for architecture details
2. Follow [Improvement Roadmap](./improvement-roadmap.md) for implementation steps
3. Test configuration with local PostgreSQL instance
4. Set up environment-specific configurations
5. Configure CI/CD with proper environment variables

---

---
Generated on: 2025-01-27 12:00:00
