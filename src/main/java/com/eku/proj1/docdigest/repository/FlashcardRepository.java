
package com.eku.proj1.docdigest.repository;

import com.eku.proj1.docdigest.entity.Flashcard;
import com.eku.proj1.docdigest.entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository
        extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByFlashcardSet(FlashcardSet flashcardSet);

    long countByFlashcardSet(FlashcardSet flashcardSet);
}
