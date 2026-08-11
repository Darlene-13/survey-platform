ALTER TABLE surveys
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
    CONSTRAINT chk_survey_status CHECK (status IN ('DRAFT', 'LIVE', 'CLOSED'));

CREATE INDEX idx_surveys_status ON surveys(status);
