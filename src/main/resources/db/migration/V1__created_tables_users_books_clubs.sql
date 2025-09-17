CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       firstname VARCHAR(255),
                       lastname VARCHAR(255),
                       role INT NOT NULL,
                       status INT NOT NULL DEFAULT 0,
                       deleted BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       created_by VARCHAR(255),
                       modified_by VARCHAR(255)
);

CREATE TABLE clubs
(
                        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        code VARCHAR(100) NOT NULL UNIQUE,
                        name VARCHAR(255),
                        reading_day INT NOT NULL,
                        category INT NOT NULL,
                        status INT NOT NULL DEFAULT 0,
                        deleted BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_by VARCHAR(255),
                        modified_by VARCHAR(255)
);

CREATE TABLE books
(
                        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        reference_no VARCHAR(100) NOT NULL UNIQUE,
                        title VARCHAR(255) NOT NULL,
                        author VARCHAR(255),
                        link VARCHAR(255),
                        imageUrl VARCHAR(255),
                        description VARCHAR(255),
                        status INT NOT NULL DEFAULT 0,
                        deleted BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_by VARCHAR(255),
                        modified_by VARCHAR(255)
);

CREATE TABLE user_club (
                           id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           club_id BIGINT NOT NULL,
                           status INT,
                           deleted BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_by VARCHAR(255),
                           modified_by VARCHAR(255),
                           CONSTRAINT fk_user_club_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_club_club FOREIGN KEY (club_id) REFERENCES clubs (id) ON DELETE CASCADE,
                           CONSTRAINT uc_user_club UNIQUE (user_id, club_id)
);

CREATE TABLE club_book (
                           id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           club_id BIGINT NOT NULL,
                           book_id BIGINT NOT NULL,
                           status INT,
                           deleted BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_by VARCHAR(255),
                           modified_by VARCHAR(255),
                           CONSTRAINT fk_club_book_club FOREIGN KEY (club_id) REFERENCES clubs (id) ON DELETE CASCADE,
                           CONSTRAINT fk_club_book_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
                           CONSTRAINT uc_club_book UNIQUE (club_id, book_id)
);

CREATE TABLE book_vote (
                           id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           book_id BIGINT NOT NULL,
                           vote INT,
                           status INT,
                           deleted BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_by VARCHAR(255),
                           modified_by VARCHAR(255),
                           CONSTRAINT fk_book_vote_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                           CONSTRAINT fk_book_vote_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
                           CONSTRAINT uc_book_vote UNIQUE (user_id, book_id)
);
