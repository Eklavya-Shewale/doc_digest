
package com.eku.proj1.docdigest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flashcard_sets")
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public FlashcardSet() {
    }

    public FlashcardSet(Document document) {
        this.document = document;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Document getDocument() {
        return document;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
