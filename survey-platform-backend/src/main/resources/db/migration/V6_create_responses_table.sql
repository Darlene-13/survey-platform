
-- ---------------------------------------------------------------
-- Responses. email_address is denormalized from the email answer
-- to serve the indexed "filter by email" admin query.
-- ---------------------------------------------------------------
CREATE TABLE responses (
                           id             BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                           survey_id      BIGINT    NOT NULL REFERENCES surveys(id) ON DELETE RESTRICT,
                           email_address  VARCHAR(255),
                           date_responded TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_responses_survey ON responses(survey_id);
CREATE INDEX idx_responses_email  ON responses(LOWER(email_address));
