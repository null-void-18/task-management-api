Task Manager API

A Spring Boot REST API to manage users and tasks with JWT authentication, role-based access, and PostgreSQL. Swagger is integrated for easy API exploration.

Features

User Management: Register, update, delete users, assign roles (USER, ADMIN).

Task Management: Create, update, delete tasks, assign to users, track status (TODO, IN_PROGRESS, DONE).

Security: JWT authentication, role-based authorization.

Validation: DTO-level validations (@NotNull, @NotBlank, @Email).

Auditing: Automatic createdAt and updatedAt timestamps.

API Docs: Swagger UI at /swagger-ui.html.

Tech Stack

Java 17 | Spring Boot 3.5.x | Spring Security | Spring Data JPA | PostgreSQL | Swagger | Maven

Setup

Clone the repo

git clone <repo-url>
cd task-manager


Configure DB in application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update


Run the app

mvn clean install
mvn spring-boot:run


Swagger UI: http://localhost:8080/swagger-ui.html

Authentication

Register /auth/register

Login /auth/login → get JWT token

Use token in headers for protected endpoints:

Authorization: Bearer <token>

API Endpoints

Users: /users (CRUD)

Tasks: /tasks (CRUD)