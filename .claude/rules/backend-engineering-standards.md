---
description: Backend architectural and engineering standards
paths:
  - "**/src/main/**/*.java"
---

# Backend Engineering Standards

## 1. Package Structure

All backend source code must reside under the root package defined in `CLAUDE.md` using a consistent layer-based structure.

- `config` -> Spring and third-party configuration classes.
- `controller` -> REST API endpoints. Responsible only for request handling and delegation. No business logic.
- `dto` -> API request and response models (immutable).
- `entity` -> JPA persistence models mapped to database tables.
- `repository` -> Spring Data JPA interfaces for data access.
- `service` -> Business logic and transactional boundaries.
- `mapper` -> Conversion between Entity and DTO objects.
- `exceptions` -> Custom domain and application exceptions.
- `advice` -> Global exception handling using `@RestControllerAdvice`.
- `util` -> Stateless helper and utility classes.

## 2. Model & Data Layer Isolation

- DTOs represent all API request and response models.
- DTOs must be implemented as Java `record` types.
- Entities are strictly persistence models and must not be exposed via API responses.
- Repository layer must never be exposed outside the service layer.
- Mapping between Entity and DTO must be handled in the `mapper` layer.

## 3. Dependency Injection

- Use constructor injection exclusively.
- Field injection (`@Autowired` on fields) is forbidden.
- Dependencies must be declared as `private final` fields.

## 4. Service Layer

- All business logic must reside in the service layer.
- Services define transactional boundaries.
- Services may return DTOs or domain-specific results.

## 5. API Design & Validation

- APIs must follow REST conventions using correct HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
- APIs must return appropriate HTTP status codes (`200`, `201`, `204`, etc.).
- Request validation must be performed using Jakarta Validation annotations.
- Validation must be applied on request DTOs using `@Valid` in controller method signatures.

## 6. Exception Handling & Error Responses

- Use custom exceptions for domain and application-level errors.
- All exceptions must be handled centrally using `@RestControllerAdvice` in the `advice` package.
- API error responses must follow RFC 7807 `ProblemDetail` format.
- Stack traces or internal system details must never be exposed to clients.

## 7. Configuration Management

- All infrastructure and framework configuration must reside in the `config` package.
- Prefer `@ConfigurationProperties` for type-safe configuration binding.
- Avoid scattered `@Value` annotations across the codebase.
