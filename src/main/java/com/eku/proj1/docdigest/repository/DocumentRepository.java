package com.eku.proj1.docdigest.repository;

import com.eku.proj1.docdigest.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}
