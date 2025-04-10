CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    locked BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE deck (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE flashcard (
    id SERIAL PRIMARY KEY,
    deck_id BIGINT NOT NULL,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    CONSTRAINT fk_flashcard_deck
        FOREIGN KEY (deck_id)
        REFERENCES deck(id)
        ON DELETE CASCADE
);

CREATE TABLE quiz (
    id SERIAL PRIMARY KEY,
    deck_id BIGINT NOT NULL,
    question VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    bad_answer_1 VARCHAR(255) NOT NULL,
    bad_answer_2 VARCHAR(255) NOT NULL,
    bad_answer_3 VARCHAR(255) NOT NULL,
    CONSTRAINT fk_quiz_deck
        FOREIGN KEY (deck_id)
        REFERENCES deck(id)
        ON DELETE CASCADE
);

CREATE TABLE true_or_false (
    id SERIAL PRIMARY KEY,
    deck_id BIGINT NOT NULL,
    question VARCHAR(255) NOT NULL,
    true_answer VARCHAR(255) NOT NULL,
    false_answer VARCHAR(255) NOT NULL,
    CONSTRAINT fk_true_or_false_deck
        FOREIGN KEY (deck_id)
        REFERENCES deck(id)
        ON DELETE CASCADE
);