
package com.trustvault.repository;

import com.trustvault.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    // Retrieve documents belonging to a specific user
    List<Document> findByOwnerUsername(String username);

    // Retrieve a document only if it belongs to the specified user
    Optional<Document> findByIdAndOwnerUsername(
            Long id,
            String username
    );
}
