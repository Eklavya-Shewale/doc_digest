package com.eku.proj1.docdigest.service;


import com.eku.proj1.docdigest.dto.DocumentResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse uploadDocument(MultipartFile file);

    List<DocumentResponse> getUserDocuments();

    DocumentResponse getDocument(Long documentId);

    Resource downloadDocument(Long documentId);

    void deleteDocument(Long documentId);
}
