package com.eku.proj1.docdigest.controller;

import com.eku.proj1.docdigest.dto.DocumentResponse;
import com.eku.proj1.docdigest.service.DocumentService;
import org.springframework.core.io.Resource;
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

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable("id") Long id) {
        DocumentResponse response = documentService.getDocument(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocumentById(@PathVariable("id") Long id)
    {
        Resource resource = documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .header("Content-Type","application/pdf")
                .body(resource);
    }


    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable("documentId") Long documentId)
    {
        documentService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}