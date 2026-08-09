package com.eku.proj1.docdigest.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long document_id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 40, message = "Title must be less than 40 characters")
    private String title;

    private String fileName;

    @NotBlank
    private String filePath;

    private Long fileSize;

    @CreationTimestamp
    private Date uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User uploader;
}
