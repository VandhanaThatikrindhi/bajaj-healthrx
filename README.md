# Bajaj Finserv HealthRx Qualifier 1 – Spring Boot Application

## Candidate Information
Name: Vandhana Thatikrindi  
Email: vandhana.thatikrindi@gmail.com  
GitHub: https://github.com/VandhanaThatikrindhi  

---

## Project Overview

This project was developed as part of the Bajaj Finserv HealthRx Qualifier challenge for the Java role.

The application is a Spring Boot backend service that automatically performs the following operations on startup:

1. Sends a POST request to generate a webhook.
2. Receives a webhook URL and JWT access token.
3. Determines the assigned SQL problem based on registration number.
4. Generates the correct final SQL query.
5. Submits the SQL solution to the webhook endpoint using JWT authentication.

All steps execute automatically using Spring Boot's CommandLineRunner without any manual trigger.

---

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- Maven
- RestTemplate (for API communication)
- Embedded Tomcat Server
- JWT Authentication
- VS Code / Eclipse

---


## How to Run the Application

### Option 1: Run using JAR file

Download the JAR file and run:

java -jar healthrx-0.0.1-SNAPSHOT.jar


---

### Option 2: Run using Maven

mvn clean package
java -jar target/healthrx-0.0.1-SNAPSHOT.jar


---

## JAR File Download

Public downloadable JAR link:

https://raw.githubusercontent.com/VandhanaThatikrindhi/bajaj-healthrx/main/healthrx-0.0.1-SNAPSHOT.jar

---

## GitHub Repository

Project source code:

https://github.com/VandhanaThatikrindhi/bajaj-healthrx

---

## Execution Flow

On application startup:

- Spring Boot initializes embedded Tomcat server
- Webhook generation request is sent
- SQL solution is computed
- Solution is submitted using JWT authentication
- Submission confirmation is received

---

## Submission Status

Successfully completed and submitted as part of Bajaj Finserv HealthRx Qualifier.

---

## Author

Vandhana Thatikrindi  
Java Developer | Spring Boot Developer
