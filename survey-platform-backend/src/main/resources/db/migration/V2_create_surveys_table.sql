-- ---------------------------------------------------------------
-- Surveys
-- ---------------------------------------------------------------
CREATE TABLE surveys (
                         id           BIGSERIAL PRIMARY KEY,
                         name         VARCHAR(255) NOT NULL,
                         description  TEXT,
                         created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--- Index for fast survey look up
CREATE INDEX idx_surveys_name ON surveys(name);