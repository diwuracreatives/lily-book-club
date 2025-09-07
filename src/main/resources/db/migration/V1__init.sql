CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       firstname VARCHAR(255),
                       role ENUM('USER', 'ADMIN') NOT NULL,
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE clubs (
                       id BIGINT PRIMARY KEY,
                       club_id VARCHAR(100) NOT NULL UNIQUE,
                       reading_day INT NOT NULL,
                       members INT NOT NULL DEFAULT 0,
                       category INT,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE books (
                       id BIGINT PRIMARY KEY,
                       book_id VARCHAR(100) NOT NULL UNIQUE,
                       title VARCHAR(255),
                       author VARCHAR(255),
                       link VARCHAR(255),
                       imageUrl VARCHAR(255),
                       addedBy BIGINT,
                       comment VARCHAR(255),
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);
