
package com.trustvault.service;

import com.trustvault.model.AuditLog;
import com.trustvault.repository.AuditLogRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(
            String username,
            String action,
            Long documentId,
            String details) {

        AuditLog auditLog = new AuditLog();

        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setDocumentId(documentId);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getUserAuditLogs(String username) {
        return auditLogRepository
                .findByUsernameOrderByTimestampDesc(username);
    }
}
