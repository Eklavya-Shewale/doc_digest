package com.eku.proj1.docdigest.service;

import org.springframework.web.multipart.MultipartFile;

public interface GeminiVisionService {

    String analyzeImage(MultipartFile image);

    String analyzeImage(byte[] imageBytes, String contentType);
}
