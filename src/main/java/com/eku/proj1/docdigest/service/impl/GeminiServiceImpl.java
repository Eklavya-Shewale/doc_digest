package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.service.GeminiService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.genai.types.GenerateContentConfig;
import com.eku.proj1.docdigest.dto.FlashcardResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Service
public class GeminiServiceImpl implements GeminiService{

    private final Client client;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

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


    @Override
    public String generateFlashcards(String text) {
        String prompt = """
                Generate 10 useful study flashcards from the following document.
                
                Return ONLY a valid JSON array.
                Each array element must contain exactly two fields:
                "question" and "answer".
                 Example:
                 [
                  {
                    "question": "What is dependency injection?",
                     "answer": "A technique where dependencies are provided to an object."
                  }
                 ]
                 
                 Rules:
                 -Base every question and answer on the supplied document, use your or general knowledge in case of errors or as per requirement.
                 -Avoid duplicate questions.
                 -Keep answers concise and useful for revision.
                 -Do not include Markdown fences or explanations outside the JSON.
                 
                 DOCUMENT TEXT:
                 """ + text;

        var response = client.models.generateContent(model, prompt, null);

        String result = response.text();

        if(result == null || result.isBlank())
        {
            throw new RuntimeException("Gemini returned an empty flashcard response.");
        }

        try {
            List<FlashcardResponse> flashcards = objectMapper.readValue(
                    result, new TypeReference<List<FlashcardResponse>>(){}
            );

            if(flashcards.isEmpty())
            {
                throw new RuntimeException("Gemini generated no flashcards");
            }

            return objectMapper.writeValueAsString(flashcards);
        }
        catch(com.fasterxml.jackson.core.JsonProcessingException e){
            throw new RuntimeException("Could not parse Gemini flashcards as JSON", e);
        }
    }
}
