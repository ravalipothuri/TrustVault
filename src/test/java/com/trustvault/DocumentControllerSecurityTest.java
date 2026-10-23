
package com.trustvault;

import com.trustvault.controller.DocumentController;
import com.trustvault.model.Document;
import com.trustvault.service.AuditLogService;
import com.trustvault.service.DocumentService;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DocumentControllerSecurityTest {

    @Test
    void userBCannotDeleteUserADocument() {

        DocumentService documentService =
                mock(DocumentService.class);

        AuditLogService auditLogService =
                mock(AuditLogService.class);

        DocumentController controller =
                new DocumentController(
                        documentService, auditLogService);

        Authentication authentication =
                mock(Authentication.class);

        when(authentication.getName()).thenReturn("userB");

        when(documentService.getUserDocument(12L, "userB"))
                .thenReturn(Optional.empty());

        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();

        String result = controller.deleteDocument(
                12L, authentication, redirectAttributes);

        assertEquals("redirect:/documents", result);

        verify(documentService, never())
                .deleteDocument(any(Document.class));

        verify(auditLogService, never())
                .logAction(anyString(), anyString(),
                        any(), anyString());

        assertEquals(
                "Document not found or access denied.",
                redirectAttributes.getFlashAttributes().get("message")
        );
    }
}
