CREATE TABLE certificates (
                              id           BIGSERIAL PRIMARY KEY,
                              answer_id    BIGINT       NOT NULL REFERENCES response_answers(id) ON DELETE CASCADE,
                              file_name    VARCHAR(255) NOT NULL,
                              content_type VARCHAR(100) NOT NULL DEFAULT 'application/pdf',
                              file_size    BIGINT       NOT NULL,
                              file_data    BYTEA        NOT NULL,
                              uploaded_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_certificates_answer ON certificates(answer_id);