CREATE TABLE club_categories (
                               id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255) NOT NULL UNIQUE,
                               role INT NOT NULL,
                               status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                               deleted BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               created_by VARCHAR(255),
                               modified_by VARCHAR(255)
);

ALTER TABLE users
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

UPDATE users
SET status = 'ACTIVE'
WHERE status = 0;


ALTER TABLE clubs
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE clubs DROP COLUMN category;

ALTER TABLE clubs
    ADD COLUMN category_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_club_category
        FOREIGN KEY (category_id)
            REFERENCES club_categories (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;

UPDATE clubs
SET status = 'ACTIVE'
WHERE status = 0;


ALTER TABLE books
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

UPDATE books
SET status = 'ACTIVE'
WHERE status = 0;

ALTER TABLE user_club
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

UPDATE user_club
SET status = 'ACTIVE'
WHERE status = 0;

ALTER TABLE club_book
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';

UPDATE club_book
SET status = 'ACTIVE'
WHERE status = 0;

ALTER TABLE book_vote
    MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    MODIFY COLUMN vote VARCHAR(50);

UPDATE book_vote
SET status = 'ACTIVE'
WHERE status = 0;
