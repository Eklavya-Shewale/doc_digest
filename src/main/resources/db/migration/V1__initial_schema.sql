CREATE TABLE `user` (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_user_email (email)
);

CREATE TABLE document (
                          document_id BIGINT NOT NULL AUTO_INCREMENT,
                          file_name VARCHAR(255),
                          file_path VARCHAR(255) NOT NULL,
                          file_size BIGINT,
                          title VARCHAR(40) NOT NULL,
                          upload_date DATETIME(6) NOT NULL,
                          user_id BIGINT NOT NULL,
                          extracted_text LONGTEXT,
                          PRIMARY KEY (document_id),
                          KEY idx_document_user_id (user_id),
                          CONSTRAINT fk_document_user
                              FOREIGN KEY (user_id) REFERENCES `user` (id)
);

CREATE TABLE flashcard_sets (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                created_at DATETIME(6) NOT NULL,
                                document_id BIGINT NOT NULL,
                                PRIMARY KEY (id),
                                KEY idx_flashcard_sets_document_id (document_id),
                                CONSTRAINT fk_flashcard_sets_document
                                    FOREIGN KEY (document_id) REFERENCES document (document_id)
);

CREATE TABLE flashcards (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            answer TEXT NOT NULL,
                            question VARCHAR(1000) NOT NULL,
                            flashcard_set_id BIGINT NOT NULL,
                            PRIMARY KEY (id),
                            KEY idx_flashcards_flashcard_set_id (flashcard_set_id),
                            CONSTRAINT fk_flashcards_flashcard_set
                                FOREIGN KEY (flashcard_set_id) REFERENCES flashcard_sets (id)
);