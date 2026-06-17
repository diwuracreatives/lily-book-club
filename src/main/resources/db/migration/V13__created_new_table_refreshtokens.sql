CREATE TABLE refresh_tokens (
                       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       token VARCHAR(100),
                       user_id BIGINT NOT NULL,
                       expiry_time TIMESTAMP,
                       deleted BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       created_by VARCHAR(255),
                       modified_by VARCHAR(255),
                       CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                       CONSTRAINT uc_refresh_tokens UNIQUE (user_id)
);

