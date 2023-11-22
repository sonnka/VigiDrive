INSERT INTO users
(user_id, first_name, last_name, email, password, avatar, `role`, keycloak_id)
VALUES (1, 'Harry', 'Potter', 'potter@gmail.com', '123456', '', 'ADMIN', '');

INSERT INTO admins
    (user_id)
VALUES (1);