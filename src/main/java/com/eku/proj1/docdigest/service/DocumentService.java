package com.eku.proj1.docdigest.service;


import com.eku.proj1.docdigest.dto.DocumentResponse;

import com.eku.proj1.docdigest.dto.FlashcardResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import com.eku.proj1.docdigest.dto.DocumentTextResponse;
import org.springframework.data.domain.Page;
import java.util.List;
import com.eku.proj1.docdigest.dto.FlashcardSetResponse;

public interface DocumentService {

    DocumentResponse uploadDocument(MultipartFile file);

    List<DocumentResponse> getUserDocuments();

    DocumentResponse getDocument(Long documentId);

    DocumentTextResponse getDocumentText(Long documentId);

    Resource downloadDocument(Long documentId);

    void deleteDocument(Long documentId);

    String summarizeDocument(Long documentId);

    Page<DocumentResponse> searchDocuments(
            String search,
            String sortBy,
            String sortDirection,
            int page,
            int size
    );

    List<FlashcardSetResponse> getFlashcardSets(Long documentId);

    List<FlashcardResponse> generateFlashcards(Long documentId);

    List<FlashcardResponse> getFlashcardsFromSet(Long documentId, Long setId);


}
