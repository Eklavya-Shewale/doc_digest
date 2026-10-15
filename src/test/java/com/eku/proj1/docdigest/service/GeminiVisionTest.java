
package com.eku.proj1.docdigest.service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GeminiVisionTest {

    @Test
    void shouldAnalyzeImage() throws IOException {
        String model = "gemini-3.1-flash-lite";

        byte[] imageBytes =
                Files.readAllBytes(Path.of("ocr-test.png"));

        Client client = new Client();

        Content content = Content.fromParts(
                Part.fromText("""
                        Describe this image.
                        Identify the text and explain what the image shows.
                        """),
                Part.fromBytes(imageBytes, "image/png")
        );

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        content,
                        null
                );

        String result = response.text();

        System.out.println("Vision Model Response:");
        System.out.println(result);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}
