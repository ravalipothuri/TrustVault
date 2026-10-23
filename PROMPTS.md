# TrustVault - AI Usage and Prompts

## 1. Purpose

AI assistance was used during the development of TrustVault to understand Spring Boot concepts, troubleshoot errors, improve security, and prepare project documentation.

## 2. Prompts Used

### Prompt 1: Project Setup
"Guide me step by step to build a beginner-friendly secure document management application using Java Spring Boot and PostgreSQL."

**Usage:** Helped plan the project structure and setup.

### Prompt 2: Authentication
"Help me configure Spring Security login and logout for a Spring Boot application."

**Usage:** Helped implement form-based authentication and protected routes.

### Prompt 3: Document Authorization
"Help me ensure that one user cannot view, download, or delete another user's documents."

**Usage:** Helped implement document ownership checks.

### Prompt 4: File Upload and Download
"Help me implement secure document upload, download, and deletion with server-generated filenames."

**Usage:** Helped develop document file-handling functionality.

### Prompt 5: Security Testing
"Help me test document access restrictions and verify that unauthorized users cannot access another user's files."

**Usage:** Helped guide manual security checks and automated testing.

### Prompt 6: Troubleshooting
"Help me troubleshoot Spring Boot startup errors, database connection issues, and browser errors step by step."

**Usage:** Helped diagnose and resolve development issues.

### Prompt 7: Security Documentation
"Help me prepare a threat model describing document access risks, file upload risks, and security mitigations."

**Usage:** Helped prepare the THREATS.md document.

## 3. Human Verification

AI-generated suggestions were reviewed and tested during development. The application was run locally, document access was checked using separate demonstration accounts, and Maven tests were executed.

## 4. Limitations

AI assistance does not guarantee that an application is completely secure. Further security review, file-content validation, production-grade account management, and deployment hardening are needed before production use.