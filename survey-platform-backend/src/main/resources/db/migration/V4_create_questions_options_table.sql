-- ---------------------------------------------------------------
-- Options for choice questions
-- ---------------------------------------------------------------
CREATE TABLE question_options (
                                  id          BIGSERIAL PRIMARY KEY,
                                  question_id BIGINT       NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
                                  value       VARCHAR(100) NOT NULL,
                                  label       VARCHAR(255) NOT NULL,
                                  sort_order  INT          NOT NULL DEFAULT 0
);

CREATE INDEX idx_options_question ON question_options(question_id);
