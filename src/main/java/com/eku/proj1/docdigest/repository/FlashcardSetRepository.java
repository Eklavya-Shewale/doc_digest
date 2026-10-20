
package com.eku.proj1.docdigest.repository;

import com.eku.proj1.docdigest.entity.Document;
import com.eku.proj1.docdigest.entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

    List<FlashcardSet> findByDocumentOrderByCreatedAtDesc(Document document);
}
