package com.eku.proj1.docdigest.repository;

import com.eku.proj1.docdigest.entity.User;
import java.util.List;
import java.util.Optional;

import com.eku.proj1.docdigest.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByUploader(User uploader);
    Optional<Document> findByIdAndUploader(Long id, User uploader);
    Page<Document> findByUploaderAndTitleContainingIgnoreCaseOrUploaderAndFileNameContainingIgnoreCase(
            User uploader1,
            String title,
            User uploader2,
            String fileName,
            Pageable pageable
    );
}
