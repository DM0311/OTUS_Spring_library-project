CREATE TABLE IF NOT EXISTS genres
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE INDEX idx_genres_name ON genres (name);