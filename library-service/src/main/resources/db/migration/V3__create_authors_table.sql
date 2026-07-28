CREATE TABLE IF NOT EXISTS authors
(
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE INDEX idx_authors_name ON authors (full_name);