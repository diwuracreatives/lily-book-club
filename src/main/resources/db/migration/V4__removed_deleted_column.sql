ALTER TABLE club_categories
DROP COLUMN deleted;

ALTER TABLE users
DROP COLUMN deleted;

ALTER TABLE clubs
DROP COLUMN deleted;

ALTER TABLE books
DROP COLUMN deleted;

ALTER TABLE user_clubs
DROP COLUMN deleted;

ALTER TABLE club_books
DROP COLUMN deleted;

ALTER TABLE book_votes
DROP COLUMN deleted;