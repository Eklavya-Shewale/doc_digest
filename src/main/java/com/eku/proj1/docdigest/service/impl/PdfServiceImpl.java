package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.service.PdfService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public String extractText(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath)))
        {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            return pdfTextStripper.getText(document);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not extract text from PDF",e);
        }
    }
}
