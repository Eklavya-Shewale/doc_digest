package com.eku.proj1.docdigest.service.impl;


import com.eku.proj1.docdigest.dto.DocumentResponse;
import com.eku.proj1.docdigest.repository.DocumentRepository;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    private final Path uploadPath = Paths.get("uploads");

    @Override
    public DocumentResponse uploadDocument(MultipartFile file) {
        if(file.isEmpty())
        {
            throw new IllegalArgumentException("File cannot be Empty!");
        }

        try {
            Files.createDirectories(uploadPath);

            String storedFileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path targetPath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetPath);

        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }


    }

    @Override
    public List<DocumentResponse> getUserDocuments() {
        return List.of();
    }

    @Override
    public DocumentResponse getDocument(Long documentId) {
        return null;
    }

    @Override
    public Resource downloadDocument(Long documentId) {
        return null;
    }

    @Override
    public void deleteDocument(Long documentId) {

    }
}
