INSERT INTO users
    (user_id, name, surname, email, password, avatar, `role`, keycloak_id)
VALUES (1, 'Harry', 'Potter', 'potter@gmail.com', '123456', '', 'ADMIN', '');

INSERT INTO admins
    (user_id)
VALUES (1);