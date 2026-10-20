package com.eku.proj1.docdigest.service.impl;


import com.eku.proj1.docdigest.dto.DocumentResponse;
import com.eku.proj1.docdigest.dto.DocumentTextResponse;
import com.eku.proj1.docdigest.entity.Document;
import com.eku.proj1.docdigest.entity.User;
import com.eku.proj1.docdigest.repository.DocumentRepository;
import com.eku.proj1.docdigest.repository.UserRepository;
import com.eku.proj1.docdigest.service.DocumentService;
import com.eku.proj1.docdigest.service.PdfService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.eku.proj1.docdigest.service.GeminiService;
import com.eku.proj1.docdigest.service.GeminiVisionService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import com.eku.proj1.docdigest.dto.FlashcardResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
    private final PdfService pdfService;
    private final GeminiService geminiService;
    private final GeminiVisionService geminiVisionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapper modelMapper,
                               UserRepository userRepository, PdfService pdfService,GeminiService geminiService, GeminiVisionService geminiVisionService) {
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.pdfService = pdfService;
        this.geminiVisionService = geminiVisionService;
        this.geminiService = geminiService;
    }

    private final Path uploadPath = Paths.get("uploads");

    @Override
    public DocumentResponse uploadDocument(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be Empty!");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10 MB");
        }
        try {
            Files.createDirectories(uploadPath);

            String storedFileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();
            //uploads/UUID_notes.pdf
            Path targetPath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetPath);

            String extractedText = pdfService.extractText(targetPath.toString());

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
            document.setExtractedText(extractedText);

            Document savedDocument = documentRepository.save(document);

            return modelMapper.map(savedDocument, DocumentResponse.class);

        } catch (IOException e) {
            throw new RuntimeException("Could not process uploaded PDF", e);
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
    public DocumentResponse getDocument(Long id) {
       String email = SecurityContextHolder.getContext()
               .getAuthentication()
               .getName();

       User user = userRepository.findByEmail(email)
               .orElseThrow(()->new RuntimeException("User not found"));

       Document document = documentRepository.findByIdAndUploader(id, user)
               .orElseThrow(()->new RuntimeException("Document Not Found"));

       return modelMapper.map(document, DocumentResponse.class);
    }


    @Override
    public DocumentTextResponse getDocumentText(Long documentId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document document = documentRepository
                .findByIdAndUploader(documentId, user)
                .orElseThrow(() -> new RuntimeException("Document Not Found"));

        return new DocumentTextResponse(
                document.getId(),
                document.getFileName(),
                document.getExtractedText()
        );
    }


    @Override
    public Resource downloadDocument(Long documentId) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Document document = documentRepository.findByIdAndUploader(documentId,user)
                .orElseThrow(()->new RuntimeException("Document Not Found"));

        Path filePath = Paths.get(document.getFilePath());

        Resource resource = new FileSystemResource(filePath);

        if(!resource.exists())
        {
            throw new RuntimeException("Document Not Found");
        }
        return resource;
    }

    @Override
    public void deleteDocument(Long documentId) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        Document document = documentRepository.findByIdAndUploader(documentId , user)
                .orElseThrow(()->new RuntimeException("Document Not Found"));

        try{
            Path filePath = Paths.get(document.getFilePath());
            Files.deleteIfExists(filePath);

            documentRepository.delete(document);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could Not Delete Document",e);
        }

    }

    @Override
    public String summarizeDocument(Long documentId) {
      String email = SecurityContextHolder.getContext()
              .getAuthentication()
              .getName();

      User user = userRepository.findByEmail(email)
              .orElseThrow(()->new RuntimeException("User Not Found"));

      Document document = documentRepository.findByIdAndUploader(documentId, user)
              .orElseThrow(()->new RuntimeException("Document Not Found"));

      String extractedText = document.getExtractedText();

      String visualAnalysis = analyzePdfPages(document.getFilePath());

      StringBuilder combinedContent = new StringBuilder();;

      if(extractedText != null && !extractedText.isBlank())
      {
          combinedContent.append("EXTRACTED PDF TEXT:\n")
                  .append(extractedText)
                  .append("\n\n");
      }

      if(!visualAnalysis.isBlank())
      {
          combinedContent.append("VISUAL ANALYSIS OF PDF PAGES: \n")
                  .append(visualAnalysis);
      }

      if(combinedContent.isEmpty())
      {
          throw new IllegalArgumentException(
                  "No text or visual content couldd be extracted from this document");

      }
      return geminiService.summarizeText(combinedContent.toString());
    }

    private String analyzePdfPages(String filePath)
    {
        StringBuilder visualAnalysis = new StringBuilder();

        try(PDDocument pdfDocument = Loader.loadPDF(Paths.get(filePath).toFile()))
        {
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);;

            for(int page = 0; page<pdfDocument.getNumberOfPages(); page++)
            {
                BufferedImage image = pdfRenderer.renderImageWithDPI(
                        page, 100, ImageType.RGB);

                try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
                {
                    ImageIO.write(image, "png", outputStream);

                    String analysis = geminiVisionService.analyzeImage(outputStream.toByteArray(), "image/png");

                    visualAnalysis.append("Page ")
                            .append(page+1)
                            .append(" visual analysis:\n")
                            .append(analysis)
                            .append("\n\n");
                }
            }
        } catch(IOException e)
        {
            throw new RuntimeException("Could not Analyze PDF Pages" , e);
        }
        return visualAnalysis.toString();
    }

    @Override
    public Page<DocumentResponse> searchDocuments(
            String search,
            String sortBy,
            String sortDirection,
            int page,
            int size)
    {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User Not Found"));;

                List<String> allowedSortFields = List.of("title", "uploadDate", "fileSize", "fileName");

                if(!allowedSortFields.contains(sortBy))
                {
                    throw new IllegalArgumentException("Invalid sort field");
                }

                if(page < 0 || size < 1 || size > 100)
                {
                    throw new IllegalArgumentException("Invalid page or size");
                }

                //Determine ASC or DESC Order
                Sort.Direction direction = Sort.Direction.fromString(sortDirection);

                Pageable pageable = PageRequest.of(
                        page, size, Sort.by(direction, sortBy)
                );

                //To return all current user documents, enter empty search string.
                String keyword = search == null ? "" : search.trim();

                Page<Document> documents = documentRepository.findByUploaderAndTitleContainingIgnoreCaseOrUploaderAndFileNameContainingIgnoreCase(
                        user, keyword, user, keyword, pageable
                );

                //Converting each document entity into a document Response DTO

        return documents.map(document -> modelMapper.map(document, DocumentResponse.class));
    }

    @Override
    public List<FlashcardResponse> generateFlashcards(Long documentId)
    {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        Document document = documentRepository.findByIdAndUploader(documentId,user)
                .orElseThrow(()->new RuntimeException("Document Not Found"));

        String extractedText = document.getExtractedText();

        if(extractedText == null || extractedText.isBlank())
        {
            throw new IllegalArgumentException("No extracted text available for flashcard generation");
        }

        String json = geminiService.generateFlashcards(extractedText);

        try{
            return objectMapper.readValue(json, new TypeReference<List<FlashcardResponse>>(){});
        }
        catch(com.fasterxml.jackson.core.JsonProcessingException e){
            throw new RuntimeException("Could not parse generated flashcards", e);
        }
    }

}
