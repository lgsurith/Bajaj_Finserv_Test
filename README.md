# Bajaj Finserv Health Qualifier

Spring Boot application that solves a SQL problem and submits the solution via webhook. Built for the Bajaj Finserv Health Qualifier round.

## Problem Statement
Given a database with three tables (DEPARTMENT, EMPLOYEE, PAYMENTS), find the highest salary that was credited to an employee, but only for transactions that were NOT made on the 1st day of any month. The solution includes employee details like name, age, and department.

## Features
- Automatic webhook generation on startup
- SQL query execution for salary analysis
- JWT-based authentication for solution submission
- Detailed console logging

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Git

## Setup and Running

1. Clone the repository:
```
git clone https://github.com/your-username/bfh-qualifier.git
cd bfh-qualifier
```

2. Build the project:
```
mvn clean package -DskipTests
```

3. Run the application:
```
java -jar target/bfh-qualifier-0.0.1-SNAPSHOT.jar
```

## Project Structure
```
src/main/java/com/surithbajaj/bfh_qualifier/
├── BfhQualifierApplication.java    # Main application class
├── dto/                           # Data Transfer Objects
│   ├── SolutionRequest.java
│   ├── WebhookRequest.java
│   └── WebhookResponse.java
└── service/
    └── QualifierService.java      # Core business logic
```

## Implementation Details

### Webhook Generation
- POST request to generate webhook URL and access token
- Endpoint: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`

### SQL Solution
- Finds highest salary not paid on 1st day of any month
- Joins PAYMENTS, EMPLOYEE, and DEPARTMENT tables
- Returns SALARY, NAME, AGE, and DEPARTMENT_NAME

### Solution Submission
- Uses JWT token for authentication
- Endpoint: `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`

## Build Output
The final JAR file is available at:
```
target/bfh-qualifier-0.0.1-SNAPSHOT.jar
```

## Author
- **Name**: Surith L G
- **Registration**: 22BLC1247
- **Email**: surithcodes204@gmail.com

## License
This project is created for the Bajaj Finserv Health qualification round and is not licensed for public use.