package com.eku.proj1.docdigest.service.impl;


import com.eku.proj1.docdigest.dto.DocumentResponse;
import com.eku.proj1.docdigest.entity.Document;
import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.repository.DocumentRepository;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
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
            //uploads/UUID_notes.pdf
            Path targetPath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetPath);

            String email = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Document document = new Document();

            document.setTitle(file.getOriginalFilename());
            document.setFileName(file.getOriginalFilename());
            document.setFilePath(targetPath.toString());
            document.setFileSize(file.getSize());
            document.setUploader(user);

            Document savedDocument = documentRepository.save(document);

            return modelMapper.map(savedDocument, DocumentResponse.class);

        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }


    }

    @Override
    public List<DocumentResponse> getUserDocuments() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User savedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Document> userDocuments = documentRepository.findByUploader(savedUser);

        return userDocuments.stream()
                .map(document -> modelMapper.map(document, DocumentResponse.class))
                .toList();
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
