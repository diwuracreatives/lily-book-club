
CREATE TABLE club_requests
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10),
    name VARCHAR(50),
    category VARCHAR(100),
    description VARCHAR(255),
    approval_status VARCHAR(50),
    user_id BIGINT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    CONSTRAINT fk_club_requests_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uc_club_requests UNIQUE (user_id)
);
