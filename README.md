# Respondly

Respondly is a full-stack interview survey platform for creating structured interview forms, sharing them with respondents, and collecting candidate responses. Administrators manage interviews and questions from a dedicated dashboard, while respondents use a focused portal to complete assigned interviews.

## Features

### Administrator

- Secure email-based authentication with JWT access and refresh tokens
- Dashboard showing draft, published, and closed interviews
- Interview creation and editing
- Short text, long text, email, choice, and certificate-upload questions
- Required and optional questions
- Publish and close interview controls
- Shareable respondent interview links
- Respondent account registration
- Review of submitted responses and uploaded certificates through the API

### Respondent

- Account registration and email-based sign-in
- Access to published interviews through a shared link
- Structured interview response form
- Choice and text answers
- Certificate upload support

## Technology stack

| Area | Technology |
| --- | --- |
| Frontend | Angular 22, TypeScript, Angular Router, Angular Forms |
| Backend | Java 25, Spring Boot 4.1, Spring Security, Spring Data JPA |
| Authentication | JWT access tokens and refresh tokens |
| API format | XML, plus multipart form data for response submissions |
| Local database | H2 in-memory database |
| Production database support | PostgreSQL with Flyway migrations |
| Build tools | npm and Maven Wrapper |

## Repository structure

```text
survey-platform/
├── survey-platform-backend/       Spring Boot REST API
│   └── src/main/
│       ├── java/.../
│       │   ├── authentication/    Login, registration, JWT and refresh tokens
│       │   ├── survey/            Interview lifecycle
│       │   ├── question/          Interview questions and options
│       │   ├── response/          Candidate responses and answers
│       │   ├── certificate/       Uploaded certificate handling
│       │   └── configuration/     Security and application configuration
│       └── resources/
│           └── db/migration/      Flyway migrations
├── survey-platform-ui/            Angular application
│   └── src/app/
│       ├── core/                  API services, guards, models and interceptors
│       └── features/              Auth, admin and respondent screens
└── docs/                          Architecture and database diagrams
```

## Local development

### Requirements

- Java 25
- Node.js 20 or newer
- npm 10 or newer

The default development configuration uses an in-memory H2 database, so PostgreSQL is not required to try the application locally. Data is cleared whenever the backend restarts.

### 1. Start the backend

```bash
cd survey-platform-backend
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`. The local H2 console is available at `http://localhost:8080/h2-console`.

### 2. Start the frontend

In another terminal:

```bash
cd survey-platform-ui
npm install
npm start
```

Open `http://localhost:4200`. Angular proxies `/api` requests to the Spring Boot server through `proxy.conf.json`.

## Local accounts

The backend creates development accounts for testing:

| Role | Email | Password |
| --- | --- | --- |
| Administrator | `admin@respondly.local` | `Admin123!` |
| Respondent | `respondent@respondly.local` | `Respondent123!` |

These credentials are for local development only. Override the JWT secret and disable or replace seeded accounts before deploying.

## Typical workflow

1. Sign in as the administrator at `/signin/admin`.
2. Create an interview from the admin dashboard.
3. Save the draft and add at least one question.
4. Publish the interview.
5. Copy the respondent link displayed in the interview builder.
6. Register a respondent or use the local respondent account.
7. Open the shared link, sign in as the respondent, and complete the interview.

Draft links are visible to administrators, but an interview must be published before it is ready for respondents.

## API overview

All API routes use the `/api/v1` prefix. Except for registration and login, protected endpoints require an `Authorization: Bearer <token>` header.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| POST | `/api/v1/auth/register` | Register an account |
| POST | `/api/v1/auth/login` | Authenticate with email and password |
| POST | `/api/v1/auth/refresh` | Exchange a refresh token |
| POST | `/api/v1/auth/logout` | Revoke a refresh token |
| GET, POST | `/api/v1/surveys` | List or create interviews |
| GET, PUT, DELETE | `/api/v1/surveys/{surveyId}` | Read, update, or delete an interview |
| PATCH | `/api/v1/surveys/{surveyId}/status` | Change interview status |
| POST | `/api/v1/surveys/{surveyId}/publish` | Publish an interview |
| POST | `/api/v1/surveys/{surveyId}/close` | Close an interview |
| GET, POST | `/api/v1/surveys/{surveyId}/questions` | List or add questions |
| GET, PUT, DELETE | `/api/v1/surveys/{surveyId}/questions/{questionId}` | Manage one question |
| GET, POST | `/api/v1/surveys/{surveyId}/responses` | Review or submit responses |
| GET | `/api/v1/certificates/{certificateId}` | Download an uploaded certificate |

The API primarily consumes and returns `application/xml`. Interview response submission uses `multipart/form-data` so answers and uploaded files can be sent together.

## Verification

Run backend tests:

```bash
cd survey-platform-backend
./mvnw test
```

Build the frontend:

```bash
cd survey-platform-ui
npm run build
```

The backend test suite covers authentication and the interview creation flow.

## Configuration

Local settings are in `survey-platform-backend/src/main/resources/application.properties`. For a non-local environment, configure at least:

- A persistent PostgreSQL datasource
- Flyway migrations
- A strong external JWT secret
- Production-safe CORS origins
- Disabled local development accounts

Never reuse the checked-in local JWT secret or sample passwords in production.

## Design references

![Respondly system architecture](docs/System_Architecture.png)

![Respondly entity relationship diagram](docs/erd.png)
