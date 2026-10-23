# TrustVault - Security Threat Model

## 1. Project Overview

TrustVault is a secure document management application built using Java, Spring Boot, and PostgreSQL. Authenticated users can upload, view, download, and delete their own documents.

## 2. Assets to Protect

- User documents and uploaded files
- User authentication sessions
- Document ownership records in PostgreSQL
- Files stored on the application server

## 3. Security Threats and Mitigations

### Threat 1: Unauthorized Document Access

**Risk:** A user may change a document ID in a URL to access another user's document.

**Mitigation:** TrustVault checks both the document ID and the authenticated username before allowing access. Unauthorized download attempts return a 404 response.

### Threat 2: Unauthenticated Access

**Risk:** An unauthenticated visitor may attempt to open protected pages or document endpoints.

**Mitigation:** Spring Security requires authentication for protected routes.

### Threat 3: Malicious File Uploads

**Risk:** An attacker may upload oversized or harmful files.

**Mitigation:** The application limits individual uploads to 5 MB and generates UUID-based storage filenames. File content validation and malware scanning are future improvements.

### Threat 4: Path Traversal

**Risk:** An attacker may attempt to use a manipulated file path to access files outside the upload directory.

**Mitigation:** The application generates storage filenames on the server and normalizes file paths before file operations.

### Threat 5: Cross-Site Request Forgery (CSRF)

**Risk:** A malicious website may attempt to submit unwanted actions using an authenticated user's session.

**Mitigation:** Spring Security's CSRF protection is enabled for form-based requests.

### Threat 6: Unauthorized Document Deletion

**Risk:** A user may try to delete a document belonging to another user.

**Mitigation:** TrustVault checks document ownership before deleting a document.

### Threat 7: Credential Exposure

**Risk:** Weak or exposed credentials could allow unauthorized access.

**Mitigation:** The current application uses demonstration accounts. Production deployment should use secure credential management and a persistent user-management system.

## 4. Security Tests Performed

- User A can view their own documents.
- User B can view only their own documents.
- User B receives a 404 when attempting to download User A's document.
- Maven automated tests completed successfully with BUILD SUCCESS.

## 5. Known Limitations

- Authentication currently uses in-memory demonstration accounts.
- Uploaded files are stored on the local server filesystem.
- File content validation and malware scanning are not implemented.
- Production deployment should use HTTPS, secure secret management, monitoring, and backups.

## 6. Conclusion

TrustVault uses authentication, document ownership checks, protected file operations, and automated tests to reduce unauthorized document access. Additional security hardening is required before production use.