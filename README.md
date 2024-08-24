# Task Management Application

This is a Task Management Application built using Spring Boot, PostgreSQL, and other technologies. The application allows users to create, update, retrieve, and delete tasks.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Assumptions and Considerations](#assumptions-and-considerations)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 8 or higher**
- **Maven**
- **PostgreSQL** 
- **Git** (optional, for cloning the repository)

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/your-repository.git
cd your-repository

2. Set Up PostgreSQL Database
Install PostgreSQL:

Download and install PostgreSQL from https://www.postgresql.org/download/.
Create a Database:

Log in to PostgreSQL using the psql command-line tool or any PostgreSQL GUI tool like pgAdmin.

Create a new database:

CREATE DATABASE taskmanager;
Create a User:

Create a PostgreSQL user with a password:

CREATE USER taskuser WITH PASSWORD 'password';
Grant Privileges:

Grant all privileges to the user on the taskmanagerdb database:

GRANT ALL PRIVILEGES ON DATABASE taskmanager TO taskuser;

3. Configure Application Properties
Open the src/main/resources/application.properties file and configure the PostgreSQL settings:
properties

spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanagerdb
spring.datasource.username=taskuser
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

4. Build the Project
Build the project using Maven:

mvn clean install
Running the Application
You can run the application using Maven or directly from your IDE.

Using Maven:

mvn spring-boot:run
Using IDE:
Import the project as a Maven project.
Run the TaskManagerApplication.java file from your IDE.
Accessing the Application
Once the application is running, you can access it at:


http://localhost:8080/

API Documentation
The application provides several RESTful endpoints for managing tasks. Below are some key endpoints:

Create Task: POST /api/tasks
Get All Tasks: GET /api/tasks
Get Task by ID: GET /api/tasks/{id}
Update Task: PUT /api/tasks/{id}
Delete Task: DELETE /api/tasks/{id}
Example Requests
You can test these endpoints using Postman or cURL.

bash
Copy code
# Example cURL request to create a task
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{
    "title": "New Task",
    "description": "Task Description",
    "status": "PENDING",
    "assignedTo": {
        "id": 1
    }
}'

Testing
Running Unit Tests
Unit tests are included in the src/test/java directory. You can run them using Maven:

mvn test
Alternatively, you can run the tests from your IDE.
Testing with Postman
Ensure the application is running.
Use Postman to test the different API endpoints as per the examples provided in the API Documentation.
Assumptions and Considerations
Task Assignment: It is assumed that tasks can only be assigned to existing users. Therefore, users must be created beforehand.
Status Enum: Task status is managed using an enum (Status), and valid statuses are PENDING, IN_PROGRESS, and COMPLETED.
Pagination: The application supports pagination for retrieving tasks. By default, the getAllTasks endpoint returns paginated results.
Error Handling: Custom exceptions are used to handle scenarios where resources (like tasks or users) are not found.
