
package com.eku.proj1.docdigest.service.impl;

import com.eku.proj1.docdigest.service.PdfService;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class PdfServiceImpl implements PdfService {


    @Override
    public String extractText(String filePath) {
        try (PDDocument document =
                     Loader.loadPDF(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();
            PDFRenderer renderer = new PDFRenderer(document);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(
                    "C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");

            StringBuilder result = new StringBuilder();

            for (int page = 0;
                 page < document.getNumberOfPages();
                 page++) {

                stripper.setStartPage(page + 1);
                stripper.setEndPage(page + 1);

                String pageText = stripper.getText(document);

                if (pageText != null && !pageText.isBlank()) {
                    // This page already contains extractable text.
                    result.append(pageText);
                } else {
                    // No extractable text: use OCR for this page.
                    BufferedImage image =
                            renderer.renderImageWithDPI(
                                    page, 200, ImageType.GRAY);

                    try {
                        result.append(tesseract.doOCR(image));
                    } catch (net.sourceforge.tess4j.TesseractException e) {
                        throw new RuntimeException(
                                "OCR failed on page " + (page + 1), e);
                    }
                }

                result.append("\n");
            }

            return result.toString();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not extract text from PDF", e);
        }
    }



}
