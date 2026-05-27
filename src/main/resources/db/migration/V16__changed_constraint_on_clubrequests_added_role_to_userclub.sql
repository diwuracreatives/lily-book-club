ALTER TABLE club_requests ADD CONSTRAINT uc_user_club_name UNIQUE (user_id, code);

ALTER TABLE user_clubs
    ADD COLUMN club_role VARCHAR(10) NOT NULL DEFAULT 'MEMBER';