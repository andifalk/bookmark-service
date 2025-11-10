# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **security-focused Spring Boot application** designed as an intentionally vulnerable bookmark service for security testing and education. The project demonstrates various security vulnerabilities and provides comprehensive security testing approaches covering OWASP Top 10 (2021) and OWASP ASVS 4.x.

**Important**: The `main` branch contains intentional security vulnerabilities for testing purposes. The `secure` branch contains the secure implementation.

## Technology Stack

- **Java 21** with **Spring Boot 3.3.0**
- **Maven** build system with wrapper
- **H2** in-memory database
- **Spring Data JPA** for data persistence
- **Spring Security** for authentication/authorization
- **OpenAPI/Swagger** for API documentation
- **Apache POI** for Excel file processing

## Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BookmarkServiceTest

# Run tests by category (using custom annotations)
./mvnw test -Dtest="**/*ArchitectureTest*"
./mvnw test -Dtest="**/*IntegrationTest*"
./mvnw test -Dtest="**/*UnitTest*"

# Run the application
./mvnw spring-boot:run

# Package the application
./mvnw package
```

### Security Analysis
```bash
# OWASP Dependency Check
./mvnw dependency-check:check

# SpotBugs security analysis
./mvnw spotbugs:check

# Generate SBOM (Software Bill of Materials)
./mvnw package cyclonedx:makeAggregateBom

# SonarQube analysis (requires SonarQube server)
./mvnw sonar:sonar -Dsonar.projectKey=PROJECT_KEY -Dsonar.token=TOKEN

# Semgrep security scanning
semgrep scan --config auto
```

### OWASP ZAP Security Testing
```bash
# From the zap/ directory:
cd zap/

# Baseline scan (passive)
./zap-baseline-scan.sh

# API scan (active)
./zap-api-scan.sh

# Full scan (comprehensive active)
./zap-full-scan.sh
```

## Architecture

### Layered Architecture
The application follows a classic 3-layered architecture enforced by ArchUnit tests:

- **API Layer** (`com.example.bookmark.api`): REST controllers
- **Service Layer** (`com.example.bookmark.service`): Business logic
- **Data Layer** (`com.example.bookmark.data`): JPA entities and repositories

### Key Components

**REST Controllers**:
- `BookmarkRestController`: Bookmark CRUD operations
- `UserRestController`: User management
- `MainRestController`: General endpoints

**Services**:
- `BookmarkService`: Core bookmark business logic
- `UserService`: User management logic
- `BookmarkUserDetailsService`: Spring Security integration

**Data Access**:
- JPA repositories with custom query methods
- Custom repository implementations for complex queries
- H2 in-memory database for development/testing

### Security Configuration
- **Authentication**: Basic Auth + Form Login
- **Authorization**: Intentionally broken for security testing
- **Password Encoding**: MD5 (intentionally weak)
- **CORS**: Permissive configuration for testing

## Testing Strategy

### Test Categories (Custom Annotations)
- `@UnitTest`: Unit-level tests
- `@IntegrationTest`: Integration tests
- `@ArchitectureTest`: Architecture compliance tests

### Security Test Structure
```
src/test/java/com/example/bookmark/security/
├── architecture/          # ArchUnit tests
│   ├── general/          # General architecture rules
│   └── security/         # Security-specific rules
├── integration/          # Security integration tests
│   ├── brokenaccesscontrol/
│   ├── brokenauthentication/
│   ├── injection/
│   ├── inputvalidation/
│   └── misconfiguration/
└── unit/                 # Security unit tests
```

## API Access

**Base URL**: http://localhost:9090
**API Documentation**: http://localhost:9090/swagger-ui.html
**OpenAPI Spec**: http://localhost:9090/v3/api-docs

### Sample API Calls
```bash
# Get bookmarks for user (HTTPie)
http :9090/api/bookmarks?userid=c9caa4d1-5ad7-4dd1-8bd1-91b8bc5b9a48 --auth bruce.wayne@example.com:wayne

# Get all users (broken authorization)
http :9090/api/users --auth bruce.wayne@example.com:wayne
```

## File Upload Testing

The application includes file upload functionality with sample files in `src/main/resources/upload/`:
- `bookmarks.xlsx` - Valid Excel file
- `bookmarks.pdf` - PDF file
- `invalid_type.xlsx` - For testing validation
- `big.pdf` - For size limit testing

## Security Testing Focus Areas

1. **Broken Authentication**: Weak password policies, MD5 encoding
2. **Broken Access Control**: Missing authorization checks
3. **Injection**: SQL injection vulnerabilities
4. **Security Misconfiguration**: Permissive CORS, debug enabled
5. **Input Validation**: File upload security, data validation
6. **Architecture**: Layer violations, security annotations

## Development Notes

- The application runs on port **9090** (not the default 8080)
- Database is wiped on restart (H2 in-memory)
- Security debug logging is enabled
- All management endpoints are exposed for testing
- CSRF is disabled for API testing convenience
- The `DataInitializer` class provides test users and bookmarks

## Branch Strategy

- **main**: Intentionally vulnerable version for security testing
- **secure**: Fixed version with proper security implementations

When working on security fixes, compare implementations between branches to understand the security improvements made.