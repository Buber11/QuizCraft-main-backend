SELECT * FROM pg_extension WHERE extname = 'vector';

CREATE TABLE embeddings (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT,
    embedding VECTOR(1536)
    REFERENCES FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX ON embeddings USING hnsw (embedding vector_l2_ops);

