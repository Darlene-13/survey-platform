-- ---------------------------------------------------------------
-- Questions: the metadata table driving the whole platform.
-- type stores the Java enum name (EnumType.STRING).
-- ---------------------------------------------------------------
CREATE TABLE questions (
                           id             BIGSERIAL PRIMARY KEY,
                           survey_id      BIGINT       NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
                           name           VARCHAR(100) NOT NULL,
                           type           VARCHAR(20)  NOT NULL
                               CHECK (type IN ('SHORT_TEXT','LONG_TEXT','EMAIL','CHOICE','FILE')),
                           required       BOOLEAN      NOT NULL DEFAULT TRUE,
                           text           TEXT         NOT NULL,
                           description    TEXT,
                           allow_multiple BOOLEAN      NOT NULL DEFAULT FALSE,
                           sort_order     INT          NOT NULL DEFAULT 0,
                           CONSTRAINT uq_question_name_per_survey UNIQUE (survey_id, name)
);


CREATE INDEX idx_questions_survey ON questions(survey_id);