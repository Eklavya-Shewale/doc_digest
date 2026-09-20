package com.eku.proj1.docdigest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTextResponse {

    private Long documentId;
    private String fileName;
    private String extractedText;
}