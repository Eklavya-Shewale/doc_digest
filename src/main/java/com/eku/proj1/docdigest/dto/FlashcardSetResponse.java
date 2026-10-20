
package com.eku.proj1.docdigest.dto;

import java.time.LocalDateTime;

public class FlashcardSetResponse {

    private Long setId;
    private LocalDateTime createdAt;
    private long flashcardCount;

    public FlashcardSetResponse() {
    }

    public FlashcardSetResponse(Long setId, LocalDateTime createdAt, long flashcardCount) {
        this.setId = setId;
        this.createdAt = createdAt;
        this.flashcardCount = flashcardCount;
    }

    public Long getSetId() {
        return setId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getFlashcardCount() {
        return flashcardCount;
    }
}
