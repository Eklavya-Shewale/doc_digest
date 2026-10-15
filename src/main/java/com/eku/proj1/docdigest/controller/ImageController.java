package com.eku.proj1.docdigest.controller;

import com.eku.proj1.docdigest.service.GeminiVisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final GeminiVisionService geminiVisionService;

    public ImageController(GeminiVisionService geminiVisionService)
    {
        this.geminiVisionService = geminiVisionService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile image)
    {
        if(image.isEmpty())
        {
            throw new IllegalArgumentException("Image cannot be empty");
        }

        if (image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image size must not exceed 5 MB");
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/png") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Only PNG, JPEG, and WebP images are allowed");
        }


        String result = geminiVisionService.analyzeImage(image);

        return ResponseEntity.ok(result);
    }

}
