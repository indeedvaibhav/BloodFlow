# BloodFlow

BloodFlow is a planned full-stack blood-donation coordination platform. Its product goal is to help requesters, donors, hospitals, and blood banks coordinate suitable donation candidates and track requests through verified fulfillment.

The platform is intended to **coordinate** people and institutions. It does not manufacture, store, or independently distribute blood. Matching can recommend candidates; it cannot provide medical clearance. Blood-bank protocols and qualified professionals remain responsible for compatibility, donor eligibility, and donation decisions.

> **Project status:** This repository currently contains only an early Spring Boot backend scaffold. The frontend, authentication, database schema, domain modules, APIs, matching workflow, notifications, analytics, and institutional tools described here are planned and are not implemented.

## Product problem and goal

Blood-donation coordination can depend on personal contacts, messaging groups, manually maintained lists, and repeated calls to donors who may be unavailable. This can make it difficult to find nearby potentially suitable donors and see a request's progress in one place.

BloodFlow's proposed value is to bring donor profiles, blood requests, location-aware candidate discovery, donor responses, coordination, and authorized fulfillment tracking into one system.

Product objectives include reducing the time to identify potential donors, improving discovery by blood group and location, reducing repeated communication, tracking request progress, maintaining reliable records, and eventually understanding regional demand.

## User roles

- **Donor:** register, maintain a donor profile and availability, receive invitations, respond, and view request and donation history.
- **Requester:** create a request with blood group, units, hospital, urgency, and required-by time; track responses and progress; cancel when appropriate.
- **Hospital or blood bank:** maintain institutional information, manage authorized staff, coordinate requests, and verify completed donations. Inventory and institutional analytics are later possibilities.
- **Administrator:** manage accounts, verify institutions, review reports and suspicious activity, monitor requests, and view platform statistics.

Institutional verification and role-based access are important prerequisites for sensitive hospital operations.

## Planned request and donation workflow

1. A requester creates a blood request with the required group or component, units, hospital, urgency, and required-by time.
2. The backend validates and stores the request.
3. A matching process filters and ranks possible donors.
4. Selected donors receive invitations and may accept, decline, or let an invitation expire.
5. The requester and institution coordinate the donation.
6. Authorized hospital or blood-bank personnel confirm completion.
7. Donation records update the request's fulfilled units and status.

A donor accepting an invitation does not mean that a donation has occurred. Request, donor response, and donation are distinct workflows with separate states.

## Planned matching approach

The initial matching engine is intended to be deterministic and rule-based:

1. Filter by medically reviewed compatibility rules appropriate to the requested blood component.
2. Apply availability and eligibility workflow criteria.
3. Filter by configured geographic radius.
4. Rank candidates using measurable factors such as distance, availability, urgency, and response history where appropriate.
5. Notify a selected set of candidates and record each response.

Compatibility differs by blood component; red-cell compatibility cannot simply be applied to plasma or platelets. Profile information alone cannot establish donor eligibility. Matching is a candidate recommendation, never medical advice or final clearance. A qualified blood-bank professional and applicable protocols determine whether a donation can proceed. Ranking weights should be supported by requirements and tested with sample data rather than labeled “intelligent” without evidence.

Geographic distance may initially use donor and hospital coordinates. PostGIS is a possible later addition for efficient radius queries.

## Proposed architecture

The blueprint chooses a **modular monolith**: one Spring Boot service with clear internal business modules. This keeps development, transactions, and debugging manageable while preserving boundaries that could support future extraction if evidence justifies it. Microservices, Redis, and Kafka are not initial requirements.

The target full-stack architecture is:

- **Frontend:** Next.js, React, and TypeScript.
- **API:** versioned REST endpoints, with SSE or WebSocket only where realtime updates warrant them.
- **Backend:** Java and Spring Boot with layered controller, service, repository, and persistence responsibilities.
- **Database:** PostgreSQL, with Flyway migrations; PostGIS when geographic querying is implemented.
- **Future infrastructure:** Docker, GitHub Actions, monitoring, and deployment tooling as the project approaches production.

Planned backend modules are authentication, user management, donors, hospitals, blood requests, matching, donor responses, donations, notifications, administration, analytics, and shared components.

## Proposed domain model

The initial model is expected to include:

- **User:** account identity, contact details, role, status, and verification timestamps.
- **DonorProfile:** donor-specific blood group, availability, location, and relevant history.
- **Hospital:** institutional details, location, contact information, and verification state.
- **BloodRequest:** requester, hospital, blood group or component, required and fulfilled units, urgency, deadline, and status.
- **DonorRequestResponse:** invitation and donor response, including notification and expiration times. A unique request/donor constraint should prevent duplicate invitations.
- **Donation:** coordination and completion records linked to a donor, request, and hospital, including authorized verification.
- **Notification:** user, request, message, delivery status, read state, and timestamps.

Sensitive health or eligibility information should only be collected when necessary, with appropriate consent and access controls. The donor/request association is represented through DonorRequestResponse rather than an unstructured many-to-many relationship.

## Planned lifecycle states

| Workflow | Proposed states |
| --- | --- |
| Blood request | CREATED, MATCHING, NOTIFIED, PARTIALLY_FULFILLED, FULFILLED, NO_MATCH, CANCELLED, EXPIRED |
| Donor response | PENDING, ACCEPTED, DECLINED, EXPIRED, WITHDRAWN |
| Donation | SCHEDULED, IN_PROGRESS, COMPLETED, VERIFIED, REJECTED, CANCELLED |

Transitions should be enforced by backend business rules. These states are separate because an open request, a donor's answer, and a completed donation represent different events.

## Proposed API areas

The blueprint describes these as initial API contracts; they are not implemented yet:

- **Authentication:** register, login, refresh, and logout under /api/v1/auth.
- **Users:** read and update the current user's profile under /api/v1/users/me.
- **Donors:** create and update a donor profile, view it, and change availability under /api/v1/donors.
- **Hospitals:** register, search, and retrieve institutional records under /api/v1/hospitals.
- **Requests:** create, retrieve, list the current user's requests, update, and cancel under /api/v1/requests.
- **Matching:** initiate a match and retrieve candidate results under /api/v1/requests/{id}/match.
- **Responses:** list invitations and accept or decline them under /api/v1/donors/me/invitations and /api/v1/responses.
- **Donations:** record and view donation coordination under /api/v1/donations.
- **Notifications:** list and mark notifications read under /api/v1/notifications.

Every endpoint will need explicit authorization and input-validation rules before implementation.

## Security, privacy, and trust

The system may handle personal contact details, location, and sensitive health-related information. The planned safeguards include:

- BCrypt or Argon2 password hashing, JWT-based authentication, and role-based authorization.
- Input validation, secure error handling, rate limiting, and audit logs for sensitive actions.
- HTTPS in production and secure handling of application secrets.
- Consent-based location sharing and restrictions on exposing donor contacts or precise locations.
- Data minimization, limited retention, and appropriate deletion and correction mechanisms.
- Verification and least-privilege access for institutional users.
- Security, privacy, and legal review before handling real donor or patient information.

## Testing strategy

Planned unit tests cover request validation, matching calculations, state transitions, donor filtering, and authorization. Integration and API tests should cover persistence, repositories, authentication, transactions, request creation, invitations, responses, and donation verification.

Important edge cases include no compatible candidates, no nearby candidates, donors becoming unavailable after matching, duplicate invitations, simultaneous donor acceptances, cancellation after notifications, expiry, partial fulfillment, and unauthorized verification. Testcontainers is a proposed option for database integration testing.

## Development roadmap

| Phase | Focus |
| --- | --- |
| 0. Product and architecture | Requirements, domain model, database design, API contracts |
| 1. Backend foundation | Spring Boot structure, PostgreSQL connection, configuration, health endpoint, Flyway |
| 2. Database | Migrations, entities, relationships, constraints, indexes, persistence tests |
| 3. Authentication and users | Registration, login, password hashing, JWT, roles, verification, profiles |
| 4. Donors | Profiles, blood group, location, availability, history, search |
| 5. Hospitals | Registration, verification, staff accounts, permissions, search |
| 6. Requests | Creation, updates, state, urgency, expiry, cancellation, partial fulfillment |
| 7. Matching | Reviewed compatibility rules, availability and geographic filters, ranking, tests |
| 8. Responses and donations | Invitations, accept/decline/expiry, coordination, verification, fulfillment |
| 9. Notifications | In-app and email delivery, tracking, preferences, realtime options |
| 10. Frontend | Authentication and donor, requester, hospital, and admin experiences |
| 11. Advanced features | Geographic analytics, shortage heatmaps, forecasting, institutional reports |
| 12. Production readiness | Docker, CI/CD, deployment, logging, monitoring, performance, security review |

Analytics, forecasting, heatmaps, and advanced matching are future extensions. Machine learning should only be considered after reliable historical data exists.

## Learning-first development approach

BloodFlow is intended to be developed independently with understanding as a primary goal. For each feature, define the problem and expected behavior, explain the engineering and architecture choices, implement in manageable steps, review and debug the work, and test it. Avoid generating large unexplained modules, adding technologies without a clear need, skipping tests, or treating compilation as proof of correctness.

## Definition of V1 success

The planned V1 is successful when users can authenticate; donors can maintain profiles; verified institutions can participate; requesters can create requests; reviewed rules can identify candidate donors; donors can respond; donation progress can be recorded and authorized personnel can verify it; partial and complete fulfillment work; and the workflows are tested, deployed, and documented.

## Current repository contents

The local project currently provides only a Spring Boot entry point, basic Maven setup, and a context-load test template. It does not yet include the planned frontend, database configuration, domain entities, migrations, controllers, services, repositories, authentication, or matching logic.

Current dependencies include Spring Web MVC, Spring Data JPA, Bean Validation, the PostgreSQL driver, DevTools, and Spring Boot test starters. The project targets Java 21. PostgreSQL is not configured, so provide datasource settings before expecting JPA-backed startup to work.

## Run the current backend scaffold

Requirements: Java 21 and a PostgreSQL database with connection details configured for Spring Boot.

On Windows PowerShell:

~~~powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/bloodflow"
$env:SPRING_DATASOURCE_USERNAME = "your_username"
$env:SPRING_DATASOURCE_PASSWORD = "your_password"
.\mvnw.cmd spring-boot:run
~~~

On macOS or Linux:

~~~bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bloodflow
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
./mvnw spring-boot:run
~~~

Do not put real credentials in source control. The service name is bloodflow-scaffold, and Spring Boot uses port 8080 by default once the application starts. There are no application endpoints yet.

Build and run the existing test template with:

~~~powershell
.\mvnw.cmd clean package
.\mvnw.cmd test
~~~

Use ./mvnw instead of .\mvnw.cmd on macOS or Linux. The current test only checks Spring application context loading; it does not validate BloodFlow product behavior. These commands have not been run as part of this README update.

## Current folder structure

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
.mvn/wrapper/             Maven Wrapper configuration
pom.xml                   Maven dependencies and Java version
mvnw, mvnw.cmd            Maven Wrapper launchers
~~~

The blueprint's future monorepo may add a frontend, documentation, deployment files, and modular backend packages. This repository has not reached that structure yet.

