package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.service.GeminiVisionService;

import com.eku.proj1.docdigest.service.GeminiVisionService;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GeminiVisionServiceImpl implements GeminiVisionService {

    private final Client client;
    private final String model;

    public GeminiVisionServiceImpl(@Value("${gemini.vision-model}") String model)
    {
        this.client = new Client();
        this.model = model;
    }

    @Override
    public String analyzeImage(MultipartFile image)
    {
        try{
            byte[] imageBytes = image.getBytes();

            Content content = Content.fromParts(
                    Part.fromText("""
                            Analyze this image for a student.
                            Extract any readable text.
                            Explain diagrams, charts, and equations where possible.
                            Organize the response with clear headings.
                            Do not invent details that are not visible.
                            """),
                    Part.fromBytes(
                            imageBytes, image.getContentType() != null ? image.getContentType() : "image/png")
                    );

            GenerateContentResponse response = client.models.generateContent(model,content,null);
            String result = response.text();

            if(result == null || result.isBlank())
            {
                throw new RuntimeException("Vision model returned an empty response");
            }
            return result;


        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not read uploaded image",e);
        }
    }

    @Override
    public String analyzeImage(byte[]imageBytes, String contentType)
    {
        Content content = Content.fromParts(
                Part.fromText("""
                        Analyze this image for a student.
                        Extract any readable text.
                        Explain diagrams, charts, and equations where possible.
                        Organize the response with clear headings.
                        Do not invent details that are not visible.
                        """),
                Part.fromBytes(imageBytes, contentType!=null ? contentType : "image/png")
        );

        GenerateContentResponse response = client.models.generateContent(model,content,null);

        String result = response.text();

        if(result == null || result.isBlank())
        {
            throw new RuntimeException("Vision model returned an empty response");
        }

        return result;

    }
}
