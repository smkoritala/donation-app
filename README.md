# Donation App

A standalone Spring Boot donation backend module built with Java 21, Spring Boot 3.5.4, and PostgreSQL.

Flow
- Admin token → Create cause → Add i18n → Activate
- User token → Donate → Get donations

## Features

- Public donation cause browsing
- JWT-secured donation creation
- Donation history for authenticated users
- Admin APIs for donation cause management
- i18n support for localized donation cause content
- Merge-friendly design for future modular monolith integration

## Tech Stack

- Java 21
- Spring Boot 3.5.4
- Spring Security
- Spring Data JPA
- PostgreSQL
- JJWT

## API Groups

### Public
- `GET /api/v1/public/causes`
- `GET /api/v1/public/causes/{code}`

### Authenticated
- `POST /api/v1/donations`
- `GET /api/v1/donations/me`
- `GET /api/v1/donations/me/{transactionRef}`

### Admin
- `POST /api/v1/admin/causes`
- `PUT /api/v1/admin/causes/{causeId}`
- `PUT /api/v1/admin/causes/{causeId}/status?status=ACTIVE`
- `POST /api/v1/admin/causes/{causeId}/i18n`

### Local Dev
- `POST /api/v1/dev/token`

## JWT Integration

This module does not provide login.  
It acts as a resource service and trusts JWTs issued by an external host application.

Configurable JWT settings:

- issuer
- secret
- principal claim
- roles claim
- name claim
- email claim

This makes the module reusable in a standalone app or mergeable into a larger monolith such as Serenity.

## Local Run

1. Create PostgreSQL database: `donation_db`

2. Update `application.yml` if needed

3. Start the app:
   ```bash
   mvn spring-boot:run