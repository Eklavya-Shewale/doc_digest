package com.eku.proj1.docdigest.controller;

import com.eku.proj1.docdigest.dto.DocumentResponse;
import com.eku.proj1.docdigest.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        DocumentResponse response = documentService.uploadDocument(file);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getUserDocuments() {
        List<DocumentResponse> documents = documentService.getUserDocuments();
        return ResponseEntity.ok(documents);
    }
}