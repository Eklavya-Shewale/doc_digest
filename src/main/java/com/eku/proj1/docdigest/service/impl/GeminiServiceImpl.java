package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.service.GeminiService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiServiceImpl implements GeminiService{

    private final Client client;
    private final String model;

    public GeminiServiceImpl(@Value("${gemini.model}") String model)
    {
        this.client = new Client();
        this.model = model;
    }

    @Override
    public String summarizeText(String text){

        String prompt = """
                Summarize the following document.
                Keep the important points, key facts, and conclusions.
                Use clear headings and simple language.
                Do not invent information that is not in the document.
                
                Document:
                """ + text;

        GenerateContentResponse response = client.models.generateContent(
                model, prompt, null);

        return response.text();
    }
}
