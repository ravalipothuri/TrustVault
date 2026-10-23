
package com.trustvault.service;

import com.trustvault.model.Document;
import com.trustvault.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public List<Document> getUserDocuments(String username) {
        return documentRepository.findByOwnerUsername(username);
    }

    public Optional<Document> getUserDocument(
            Long id, String username) {
        return documentRepository.findByIdAndOwnerUsername(
                id, username);
    }

    public void deleteDocument(Document document) {
        documentRepository.delete(document);
    }
}
