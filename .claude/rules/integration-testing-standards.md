---
description: Integration testing standards and guidelines
paths:
  - "**/src/test/**/*.java"
---

# Integration Testing Standards

## 1. Test Scope

- Do not mock application layers (controller, service, repository).
- Only mock external dependencies that cannot run locally.
- Tests must validate full request → service → database flow.

## 2. Test Structure

- Follow Arrange → Act → Assert pattern.
- Keep test cases focused on a single behavior.
- Use `@DisplayName` with a clear sentence alongside a short camelCase method name. Do not use underscores or overly long method names.

Example:
```java
@Test
@DisplayName("Should return all items")
void shouldReturnAllItems() { ... }

@Test
@DisplayName("Should return 404 ProblemDetail when item ID does not exist")
void shouldReturn404WhenNotFound() { ... }
```

## 3. API Testing Approach

- Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` for full application context testing.
- Assert responses using DTO contracts, not entity structures.
- Return the response body as a typed object and assert using AssertJ.

## 4. Error Response Validation

- Error responses must follow RFC 7807 `ProblemDetail`.
- Assert the correct HTTP status code for every error scenario.

## 5. Data Management

- Integration tests must run against an in-memory H2 database.
- Use `@Transactional` on test classes to ensure automatic rollback after each test.
