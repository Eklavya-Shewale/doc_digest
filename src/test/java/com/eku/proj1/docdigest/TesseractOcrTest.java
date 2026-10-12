package com.eku.proj1.docdigest;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;


class TesseractOcrTest {

    @Test
    void shouldExtractTextFromImage() throws TesseractException {
        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("eng");

        String extractedText = tesseract.doOCR(new File("ocr-test.png"));

        System.out.println(extractedText);

        assertTrue(extractedText.contains("DocDigest"));
        assertTrue(extractedText.contains("Spring Boot"));
    }
}
