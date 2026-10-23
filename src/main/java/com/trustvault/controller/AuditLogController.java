package com.trustvault.controller;

import com.trustvault.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/audit-logs")
    public String viewAuditLogs(Authentication authentication, Model model) {
        model.addAttribute("auditLogs",
                auditLogService.getUserAuditLogs(authentication.getName()));
        return "audit-logs";
    }
}
