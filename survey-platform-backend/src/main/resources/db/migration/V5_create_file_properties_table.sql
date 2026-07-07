-- ---------------------------------------------------------------
-- Upload rules for file questions (1:1 with question)
-- ---------------------------------------------------------------
CREATE TABLE file_properties (
                                 id                 BIGSERIAL PRIMARY KEY,
                                 question_id        BIGINT      NOT NULL UNIQUE REFERENCES questions(id) ON DELETE CASCADE,
                                 format             VARCHAR(20) NOT NULL DEFAULT '.pdf',
                                 max_file_size      INT         NOT NULL DEFAULT 1 CHECK (max_file_size > 0),
                                 max_file_size_unit VARCHAR(10) NOT NULL DEFAULT 'mb' CHECK (max_file_size_unit IN ('kb','mb')),
                                 allow_multiple     BOOLEAN     NOT NULL DEFAULT TRUE
);
