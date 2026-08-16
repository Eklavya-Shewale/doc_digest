package com.eku.proj1.docdigest.repository;

import com.eku.proj1.docdigest.entity.User;
import java.util.List;
import com.eku.proj1.docdigest.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByUploader(User uploader);
}
