
package com.trustvault.controller;

import com.trustvault.model.Document;
import com.trustvault.service.AuditLogService;
import com.trustvault.service.DocumentService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class DocumentController {

    private final DocumentService documentService;
    private final AuditLogService auditLogService;

    private final Path uploadDirectory =
            Paths.get("trustvault-uploads")
                    .toAbsolutePath()
                    .normalize();

    public DocumentController(
            DocumentService documentService,
            AuditLogService auditLogService) {
        this.documentService = documentService;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/documents")
    public String documents(
            Authentication authentication,
            Model model) {

        List<Document> documents =
                documentService.getUserDocuments(
                        authentication.getName());

        model.addAttribute("documents", documents);

        return "documents";
    }

    @PostMapping("/documents/upload")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message", "Please choose a file.");
            return "redirect:/documents";
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute(
                    "message", "File size must not exceed 5 MB.");
            return "redirect:/documents";
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            redirectAttributes.addFlashAttribute(
                    "message", "Invalid file name.");
            return "redirect:/documents";
        }

        String originalName = Paths.get(originalFilename)
                .getFileName()
                .toString();

        String storedName = UUID.randomUUID().toString();

        try {
            Files.createDirectories(uploadDirectory);

            Path destination = uploadDirectory
                    .resolve(storedName)
                    .normalize();

            if (!destination.startsWith(uploadDirectory)) {
                redirectAttributes.addFlashAttribute(
                        "message", "Invalid file path.");
                return "redirect:/documents";
            }

            Files.copy(file.getInputStream(), destination);

            Document document = new Document();
            document.setFileName(originalName);
            document.setStoredFileName(storedName);
            document.setContentType(
                    file.getContentType() == null
                            ? "application/octet-stream"
                            : file.getContentType());
            document.setOwnerUsername(authentication.getName());

            Document savedDocument =
                    documentService.saveDocument(document);

            auditLogService.logAction(
                    authentication.getName(),
                    "UPLOAD",
                    savedDocument.getId(),
                    "Document uploaded successfully.");

            redirectAttributes.addFlashAttribute(
                    "message", "Document uploaded successfully.");

        } catch (IOException e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "message", "Upload failed. Please try again.");
        }

        return "redirect:/documents";
    }

    @GetMapping("/documents/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        System.out.println("Download requested: ID=" + id
                + ", user=" + username);

        Document document = documentService
                .getUserDocument(id, username)
                .orElse(null);

        if (document == null) {
            System.out.println(
                    "DOWNLOAD FAILED: Document not found or access denied.");

            return ResponseEntity.notFound().build();
        }

        String storedName = document.getStoredFileName();

        if (storedName == null || storedName.isBlank()) {
            System.out.println(
                    "DOWNLOAD FAILED: Stored filename is empty.");

            return ResponseEntity.notFound().build();
        }

        Path filePath = uploadDirectory
                .resolve(storedName)
                .normalize();

        System.out.println("UPLOAD DIRECTORY: " + uploadDirectory);
        System.out.println("DOWNLOAD PATH: " + filePath);
        System.out.println("FILE EXISTS: "
                + Files.isRegularFile(filePath));
        System.out.println("LOGGED-IN USER: " + username);

        if (!filePath.startsWith(uploadDirectory)
                || !Files.isRegularFile(filePath)) {
            System.out.println(
                    "DOWNLOAD FAILED: File missing or path rejected.");

            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        String contentType = document.getContentType();

        if (contentType != null && !contentType.isBlank()) {
            try {
                mediaType = MediaType.parseMediaType(contentType);
            } catch (IllegalArgumentException ignored) {
                // Keep application/octet-stream if invalid.
            }
        }

        String filename = document.getFileName();

        if (filename == null || filename.isBlank()) {
            filename = "document";
        }

        filename = filename
                .replace("\"", "")
                .replace("\r", "")
                .replace("\n", "");

        auditLogService.logAction(
                username,
                "DOWNLOAD",
                document.getId(),
                "Document downloaded successfully.");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @PostMapping("/documents/{id}/delete")
    public String deleteDocument(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        Document document = documentService
                .getUserDocument(id, username)
                .orElse(null);

        if (document == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Document not found or access denied.");
            return "redirect:/documents";
        }

        try {
            String storedName = document.getStoredFileName();

            if (storedName != null && !storedName.isBlank()) {
                Path filePath = uploadDirectory
                        .resolve(storedName)
                        .normalize();

                if (filePath.startsWith(uploadDirectory)) {
                    Files.deleteIfExists(filePath);
                }
            }

            documentService.deleteDocument(document);

            auditLogService.logAction(
                    username,
                    "DELETE",
                    document.getId(),
                    "Document deleted successfully.");

            redirectAttributes.addFlashAttribute(
                    "message", "Document deleted successfully.");

        } catch (IOException e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Could not delete the document file. Please try again.");
        }

        return "redirect:/documents";
    }
}
