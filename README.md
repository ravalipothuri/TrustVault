# TrustVault - Secure Document Management

## Project Overview

TrustVault is a secure document management web application built using Java, Spring Boot, Spring Security, and PostgreSQL. It allows authenticated users to upload, view, download, and delete their own documents.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- PostgreSQL
- Maven
- HTML and Thymeleaf
- Git

## Features

- Login and logout using Spring Security
- Upload and manage documents
- Download and delete documents
- User-specific document ownership checks
- Audit logs for document actions
- File upload size restrictions
- UUID-based stored filenames
- Automated tests

## Prerequisites

- Java Development Kit (JDK)
- PostgreSQL
- Git

## Local Setup

1. Clone or download the project.
2. Create a PostgreSQL database named `trustvault`.
3. Configure the database URL, username, and password in `src/main/resources/application.properties`.
4. Open a terminal in the project folder.
5. Start the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

6. Open the application at `http://localhost:8081`.

## Running Tests

Run the automated tests with:

```powershell
.\mvnw.cmd test
```

A successful test run should display `BUILD SUCCESS`.

## Security Verification

The following checks were performed during development:

- User A can access their own documents.
- User B sees only their own documents.
- User B receives a 404 response when attempting to download User A's document.
- Document upload and download actions can be checked in the audit logs.

## Documentation

- `THREATS.md` - security threats, mitigations, and known limitations.
- `PROMPTS.md` - AI prompts and usage documentation.

## Known Limitations

- Demonstration accounts use in-memory authentication.
- Uploaded files are stored on the local filesystem.
- Production deployment requires additional hardening, including HTTPS, secure credential management, file validation, and backups.

## Disclaimer

TrustVault is an educational project and should undergo further security review before production use.

## Test Login Credentials

Test Account 1:
- Username: userA
- Password: TestA@123

Test Account 2:
- Username: userB
- Password: TestB@123
