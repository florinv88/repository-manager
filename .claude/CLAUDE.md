# Students Manager

## Project Overview
`students-manager` is a Student Management System for managing student records (create, read, update, delete).

## Technology Stack
- **Backend:** Java 21, Spring Boot 4.1.0, Maven
- **Database:** H2 in-memory
- **Frontend:** Vanilla JS SPA — single static HTML file served by Spring Boot
- **Testing:** JUnit 5

## Root Package
`com.vinsguru.students`

## Testing
- Integration tests only — no unit tests.
- Do not use Playwright or any E2E testing framework.

## Core Operational Commands
- Build: `mvn clean compile`
- Package: `mvn clean package`
- Run: `mvn spring-boot:run`
- Test: `mvn clean test`
