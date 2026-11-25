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

### 10. Committed the backend foundation

**What happened:** Committed the scaffold, configuration, status endpoint, and journal as `Add Spring Boot backend foundation` (`a2946cc`). Confirmed `git status --short --branch` showed a clean working tree on `master`.

**Why:** This gives us a reviewed recovery point before domain features are added.

### 11. Decided the account-to-donor-profile relationship

**Decision:** An account may exist without a donor profile. If its owner becomes a donor, that account has one donor profile, and each donor profile belongs to one account.

**Why:** Account identity is shared by requesters and donors; donor-specific data only belongs to accounts that opt into donating. The unique relationship prevents duplicate donor profiles for one account.

### 12. Chose the initial donor profile fields

**Decision:** The first version will include blood group, availability, and full address. `lastDonationDate` is deferred.

**Why:** Each field adds collection, validation, privacy, and maintenance work. The date is excluded until an agreed product behavior requires it; it will not be used to make a medical eligibility decision.

## Completed steps

### 13. Decided account capabilities

**Decision:** The same account can have a donor profile and create blood requests. Donor status is represented by the optional donor profile; the account does not need mutually exclusive donor/requester roles for the MVP.

**Why:** A person may need blood for someone and also be willing to donate. The model should allow both without inventing account roles that duplicate the profile relationship.

### 14. Created the first persistence entity

**What happened:** Added `User` in `com.bloodflow.identity` as a JPA entity mapped to `users`. It includes an identity-generated `id`, required `name`, unique required `email`, `passwordHash`, optional `phone`, and `Instant` timestamps. The timestamps are initialized on insert and `updatedAt` is refreshed on update.

**Why:** This gives identity data a persistent representation while keeping donor-specific attributes in the future donor profile. The JPA entity describes how Java should map to the database; it does not itself manage schema history.

### 15. Added and applied the first database migration

**What happened:** Added Spring Boot Flyway and PostgreSQL Flyway dependencies, and created `src/main/resources/db/migration/V1__create_users_table.sql` for the `users` table. A full application restart ran Flyway: it validated one migration and successfully applied version 1. A PostgreSQL query confirmed both `users` and `flyway_schema_history` exist in the `public` schema.

**Why:** Flyway makes database structure changes explicit and versioned. The database schema is created by a migration, rather than relying on Hibernate to silently change tables at startup.

**Related configuration:** Temporarily enabled `logging.level.org.flywaydb=DEBUG` to diagnose startup, confirmed Flyway's behavior, and then removed the setting. It is not part of the normal application configuration.

## Current step

### 16. Clean up diagnostics and checkpoint the persistence foundation

**Status:** Review passed; ready to commit.

**Review:** The app started successfully with migration version 1 applied; PostgreSQL confirmed both tables. The six intended files were staged, and `git diff --cached --check` is clean.

**Next:** Commit the `User` entity and V1 migration as a focused milestone.

**Why:** Debug logging was useful for verifying Flyway but is noisy for normal development. A commit after review gives the first database-backed feature a clear recovery point.

## Current project state

- STS project location is `D:\Projects\bloodflow`.
- The app starts locally on port 8081 and connects to database `bloodflow`.
- Flyway version 1 is applied; `users` and `flyway_schema_history` were verified in PostgreSQL.
- The actual database password is held in the local STS launch configuration and must not be committed.
- Commit `a2946cc` is the last commit; the user entity, migration, Flyway dependencies, configuration, and journal updates are currently uncommitted.
