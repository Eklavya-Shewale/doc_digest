package com.eku.proj1.docdigest.service;

import com.eku.proj1.docdigest.service.impl.PdfServiceImpl;
import org.junit.jupiter.api.Test;

public class PdfServiceTest {

    @Test
    void extractTextFromPdf() {

        PdfService pdfService = new PdfServiceImpl();

        String filePath = "C:\\Users\\Eklavya Shewale\\Downloads\\DocDigest_SRS.pdf";

        String text = pdfService.extractText(filePath);

        System.out.println(text);
    }
}