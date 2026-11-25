# BloodFlow Project Journal

A step-by-step record of how BloodFlow is being built, the decisions made, and why.

## Project direction

- Learning-first build: the project owner writes and understands the code; Codex explains choices, gives focused tasks, and reviews/debugs the work.
- Backend first: Java, Spring Boot, PostgreSQL, Maven, and Spring Data JPA.
- Architecture: modular monolith, with domain code grouped by business capability inside one application.
- Frontend and advanced infrastructure are deferred until the backend foundation is solid.

## Completed steps

### 1. Initialized the Git repository

**What happened:** Initialized Git in `D:\Projects\bloodflow`.

**Why:** Version control keeps a history of meaningful changes and makes each milestone reviewable.

### 2. Added the project README

**What happened:** Created `README.md` with a short project description, planned backend stack, and current status.

**Why:** The README is the project's front page and records the current direction without claiming unfinished features exist.

### 3. Made the first commit

**What happened:** Committed the README as `Add project README` (`b5d8e8e`). Confirmed the working tree was clean on `master`.

**Why:** This provides a clean starting point before adding the application scaffold.

### 4. Generated and copied the Spring Boot scaffold

**What happened:** Generated the Maven Spring Boot project in `D:\Projects\bloodflow-scaffold`, then copied its files into `D:\Projects\bloodflow`, preserving the repo's `.git` and existing `README.md`. The scaffold uses Java 21 and includes Spring Data JPA, Validation, Spring MVC, the PostgreSQL driver, optional DevTools, and test starters. `.gitignore` excludes generated build output and STS metadata.

**What we inspected:** `pom.xml`, `.gitignore`, the application entry point, and `application.properties`. The generated entry point is `BloodflowScaffoldApplication` in `com.bloodflow`.

**Why:** The generated scaffold establishes the build and application bootstrap while keeping the implementation incremental and understandable.

### 5. Created the local PostgreSQL database and app login

**What happened:** Confirmed PostgreSQL Server 17 was running and located `psql` 17.11 at `D:\postgres\bin\psql.exe`. Created login role `bloodflow_app`, set its password interactively, and created database `bloodflow` owned by that role.

**Why:** The application uses a dedicated database login rather than the database administrator account. The password was not recorded in this journal.

### 6. Configured and ran the staging copy

**What happened:** In the staging copy, set the Spring application name to `bloodflow`; added the PostgreSQL JDBC URL and username; supplied the password through an STS environment variable; disabled Open EntityManager in View; and selected port 8081 because `AgentService` was using port 8080. The staging copy connected to PostgreSQL 17.11 and started successfully on port 8081.

**Why:** This validated the scaffold's local database connection and web server startup. Its log revealed STS was still running the staging path rather than the repo copy.

### 7. Imported the Git repository copy into STS

**What happened:** Imported the Maven project from `D:\Projects\bloodflow`; STS Properties → Resource confirmed that location. The project was then launched from `D:\Projects\bloodflow\target\classes`.

**Why:** STS must build and run the same files Git tracks so changes are not split between the repository and staging directory.

### 8. Configured and started the repository copy

**What happened:** Updated the repo's `application.properties` with application name `bloodflow`, PostgreSQL URL and username, the `BLOODFLOW_DB_PASSWORD` environment placeholder, `spring.jpa.open-in-view=false`, and port 8081. The app connected to PostgreSQL 17.11, initialized JPA, and started Tomcat on port 8081.

**Why:** This confirms the actual Git repository project, not just the staging copy, can start with its local database.

### 9. Added and verified the first API endpoint

**What happened:** Created a status controller under `com.bloodflow.status` with `GET /api/v1/status`. Opening `http://localhost:8081/api/v1/status` returned `{"status":"UP","service":"bloodflow"}`.

**Why:** This is the first vertical slice through Spring MVC: a route maps an HTTP request to a controller method, and Spring serializes its return value as JSON. It validates the API layer without involving domain or database operations.

## Current step

### 10. Review and checkpoint the foundation

**Status:** Not started.

**Next:** Review `git status` and the changed files, confirm no password or generated output is staged, then create a checkpoint commit for the backend foundation and status endpoint.

**Why:** A focused commit gives the working foundation a clear recovery point before domain features are added.

## Current project state

- STS project location is `D:\Projects\bloodflow`.
- The app starts locally on port 8081 and connects to database `bloodflow`.
- The actual database password is held in the local STS launch configuration and must not be committed.
- The scaffold and project journal have not yet been committed as a new Git milestone.
