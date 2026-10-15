
package com.eku.proj1.docdigest.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.eku.proj1.docdigest.service.impl.GeminiVisionServiceImpl;

class GeminiVisionServiceTest {

    @Test
    void shouldAnalyzeImage() throws Exception {
        GeminiVisionService visionService =
                new GeminiVisionServiceImpl("gemini-3.1-flash-lite");

        byte[] imageBytes = java.nio.file.Files.readAllBytes(
                java.nio.file.Path.of("ocr-test.png"));

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ocr-test.png",
                "image/png",
                imageBytes
        );

        String result = visionService.analyzeImage(image);

        System.out.println("Vision Analysis:");
        System.out.println(result);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}
