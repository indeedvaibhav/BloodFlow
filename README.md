# BloodFlow Scaffold

A minimal Spring Boot backend scaffold for the BloodFlow project. At present it provides the application entry point and build structure; it does not yet implement blood-flow domain logic, REST endpoints, persistence models, or a configured database connection.

## Current contents

- Spring Boot application entry point: com.bloodflow.BloodflowScaffoldApplication
- Spring Web MVC, Spring Data JPA, and Jakarta validation dependencies
- PostgreSQL JDBC driver at runtime
- Spring Boot DevTools for development
- A context-load test template
- Maven Wrapper scripts for Windows and Unix-like systems

The project currently has no controllers, request or response models, services, entities, repositories, validation rules, or application-specific configuration. The only application property sets the service name to bloodflow-scaffold. The PostgreSQL dependency is present, but no database URL or credentials are configured.

## Requirements

- Java 21
- No separate Maven installation is needed when using the included Maven Wrapper.
- PostgreSQL is needed once database-backed functionality is configured. Supply a database connection before expecting JPA-backed startup to work.

## Run

From the project directory on Windows:

~~~powershell
.\mvnw.cmd spring-boot:run
~~~

On macOS or Linux:

~~~bash
./mvnw spring-boot:run
~~~

The application uses Spring Boot's default web-server port, 8080, once it starts successfully. No application endpoints are defined yet.

## Build and test

Windows:

~~~powershell
.\mvnw.cmd clean package
.\mvnw.cmd test
~~~

macOS or Linux:

~~~bash
./mvnw clean package
./mvnw test
~~~

The existing test is a Spring Boot context-load placeholder; it does not exercise application-specific behavior.

## Configure PostgreSQL

Before adding or starting database-backed features, configure a PostgreSQL database and provide its connection settings through environment-specific configuration or environment variables. Keep credentials out of source control. No schema, migrations, seed data, or database initialization scripts are included in this scaffold.

## Project layout

~~~text
src/
  main/
    java/com/bloodflow/
      BloodflowScaffoldApplication.java
    resources/
      application.properties
  test/
    java/com/bloodflow/
      BloodflowScaffoldApplicationTests.java
pom.xml                 Maven dependencies and Java version
mvnw, mvnw.cmd          Maven Wrapper launchers
~~~

## Dependency overview

- spring-boot-starter-webmvc — Spring MVC web application foundation
- spring-boot-starter-data-jpa — JPA and repository foundation
- spring-boot-starter-validation — request and model validation foundation
- org.postgresql:postgresql — PostgreSQL JDBC driver
- spring-boot-devtools — optional development-time restart support

## License

No project license is declared yet. Add a license before distributing or reusing the project.
