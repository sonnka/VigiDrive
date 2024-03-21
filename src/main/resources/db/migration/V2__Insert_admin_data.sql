INSERT INTO users (first_name, last_name, email, password, avatar, `role`, provider)
VALUES ('Admin', 'Admin', 'admin@gmail.com', '$2a$10$qOQe2QxYuAujlHXVA2MrYueokXjwqPx3yt5BXh/LXlrGdIsrokL5O',
        'https://icon-library.com/images/admin-login-icon/admin-login-icon-26.jpg', 'CHIEF_ADMIN', 'LOCAL');

INSERT INTO admins (user_id, date_of_approving, date_of_adding, added_by, is_approved, is_new_account)
VALUES ((SELECT user_id FROM users WHERE email = 'admin@gmail.com'), NULL, NULL, NULL, TRUE, FALSE);
