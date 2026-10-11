package com.eku.proj1.docdigest.service;

import com.eku.proj1.docdigest.service.impl.GeminiServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

 class GeminiServiceTest {
     @Test
     void summarizeText()
     {
         GeminiService geminiService = new GeminiServiceImpl("gemini-3.8-flash");

         String text = """
                 Java is an object-oriented programming language. Spring boot simplifies Java application development.
                 It provides autoconfiguration and embedded servers.
                 """;

         String summary = geminiService.summarizeText(text);

         assertNotNull(summary);
         assertFalse(summary.isEmpty());

         System.out.println("Gemini Summary: ");
         System.out.println(summary);
     }
}
