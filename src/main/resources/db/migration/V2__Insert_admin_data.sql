INSERT INTO users (first_name, last_name, email, password, avatar, `role`)
VALUES ('Admin', 'Admin', 'admin@gmail.com', '$2a$10$qOQe2QxYuAujlHXVA2MrYueokXjwqPx3yt5BXh/LXlrGdIsrokL5O', '',
        'ADMIN');

INSERT INTO admins (user_id, date_of_approving, is_approved, is_chief_admin)
VALUES ((SELECT user_id FROM users WHERE email = 'admin@gmail.com'), NULL, TRUE, TRUE);

