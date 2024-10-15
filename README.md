# CSSE BE - SE-S1-WE-02
## Hospital Management System - Backend

This is the backend service for a Hospital Management System (HMS) built using Spring Boot. It provides essential functionalities such as managing patient payments, generating patient cards, scheduling appointments, and managing medical reports. The system aims to streamline hospital operations and improve patient care through efficient data management and process automation.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Endpoints](#endpoints)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Features
This backend service provides the following core features:

- **Patient Payment Management**: Handles payment records, tracks patient bills, and processes payments.
- **Patient Card Generation**: Generates and manages unique patient ID cards for identification and record-keeping.
- **Appointment Scheduling**: Allows patients to schedule and manage appointments with doctors.
- **Medical Report Management**: Stores and organizes patient medical records for easy access and retrieval by medical staff.

## Tech Stack
- **Java (JDK 11 or later)**
- **Spring Boot** - Core framework for building RESTful web services
- **Spring Data JPA** - Data management with support for ORM
- **MySQL** - Database for storing data (or any relational database of choice)
- **Swagger** - API documentation
- **Maven** - Dependency management

## Getting Started

### Prerequisites
- **Java JDK 11 or later**: [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Maven**: [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL**: Ensure MySQL server is installed and running

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/sathninduk/csse-be.git
    cd csse-be
    ```

2. Configure the Database:
    Open the `src/main/resources/application.properties` file and update the database configurations:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/hospital_management
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Install dependencies:
    ```bash
    mvn clean install
    ```

### Running the Application
To start the server, run the following command in the project root:
```bash
mvn spring-boot:run