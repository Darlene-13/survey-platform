-- ---------------------------------------------------------------
-- Response Answers: one row per question answered in a response
-- (the EAV table connecting a submission to its individual answers)
-- ---------------------------------------------------------------
CREATE TABLE response_answers (
                                  id           BIGSERIAL PRIMARY KEY,
                                  response_id  BIGINT NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
                                  question_id  BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
                                  answer_value TEXT
);

CREATE INDEX idx_answers_response ON response_answers(response_id);
CREATE INDEX idx_answers_question ON response_answers(question_id);