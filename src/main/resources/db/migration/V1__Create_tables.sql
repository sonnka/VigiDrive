DROP TABLE IF EXISTS histories;
DROP TABLE IF EXISTS accesses;
DROP TABLE IF EXISTS health;
DROP TABLE IF EXISTS situations;
DROP TABLE IF EXISTS licenses;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS users;

CREATE TABLE histories
(
    history_id  BIGINT AUTO_INCREMENT NOT NULL,
    admin_email VARCHAR(255)          NOT NULL,
    time        datetime              NULL,
    operation   VARCHAR(255)          NULL,
    CONSTRAINT pk_histories PRIMARY KEY (history_id)
);

CREATE TABLE users
(
    user_id    BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)          NULL,
    last_name  VARCHAR(255)          NULL,
    email      VARCHAR(255)          NULL,
    password   VARCHAR(255)          NULL,
    avatar     VARCHAR(255)          NULL,
    `role`     VARCHAR(255)          NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE managers
(
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_managers PRIMARY KEY (user_id)
);

ALTER TABLE managers
    ADD CONSTRAINT FK_MANAGERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);


CREATE TABLE admins
(
    user_id           BIGINT       NOT NULL,
    date_of_approving datetime     NULL,
    date_of_adding    datetime     NULL,
    added_by          VARCHAR(255) NULL,
    is_approved       BIT(1)       NULL,
    is_new_account    BIT(1)       NULL,
    CONSTRAINT pk_admins PRIMARY KEY (user_id)
);

ALTER TABLE admins
    ADD CONSTRAINT FK_ADMINS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);


CREATE TABLE drivers
(
    user_id           BIGINT       NOT NULL,
    date_of_birth     date         NULL,
    phone_number      VARCHAR(255) NULL,
    emergency_contact VARCHAR(255) NULL,
    destination       VARCHAR(255) NULL,
    current_location  VARCHAR(255) NULL,
    manager_id        BIGINT       NULL,
    CONSTRAINT pk_drivers PRIMARY KEY (user_id)
);

ALTER TABLE drivers
    ADD CONSTRAINT FK_DRIVERS_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES managers (user_id);

ALTER TABLE drivers
    ADD CONSTRAINT FK_DRIVERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);


CREATE TABLE accesses
(
    access_id            BIGINT AUTO_INCREMENT NOT NULL,
    driver_id            BIGINT                NULL,
    manager_id           BIGINT                NULL,
    start_date_of_access datetime              NULL,
    end_date_of_access   datetime              NULL,
    access_duration      VARCHAR(255)          NULL,
    is_new               BIT(1)                NULL,
    is_active            BIT(1)                NULL,
    is_expiring          BIT(1)                NULL,
    CONSTRAINT pk_accesses PRIMARY KEY (access_id)
);

ALTER TABLE accesses
    ADD CONSTRAINT FK_ACCESSES_ON_DRIVER FOREIGN KEY (driver_id) REFERENCES drivers (user_id);

ALTER TABLE accesses
    ADD CONSTRAINT FK_ACCESSES_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES managers (user_id);


CREATE TABLE licenses
(
    license_id BIGINT AUTO_INCREMENT NOT NULL,
    number     VARCHAR(255)          NULL,
    date_to    date                  NULL,
    driver_id  BIGINT                NULL,
    CONSTRAINT pk_licenses PRIMARY KEY (license_id)
);

ALTER TABLE licenses
    ADD CONSTRAINT FK_LICENSES_ON_DRIVER FOREIGN KEY (driver_id) REFERENCES drivers (user_id);


CREATE TABLE health
(
    info_id             BIGINT AUTO_INCREMENT NOT NULL,
    time                datetime              NULL,
    stress_level        DOUBLE                NULL,
    concentration_level DOUBLE                NULL,
    sleepiness_level    DOUBLE                NULL,
    driver_id           BIGINT                NULL,
    CONSTRAINT pk_health PRIMARY KEY (info_id)
);

ALTER TABLE health
    ADD CONSTRAINT FK_HEALTH_ON_DRIVER FOREIGN KEY (driver_id) REFERENCES drivers (user_id);


CREATE TABLE situations
(
    situation_id  BIGINT AUTO_INCREMENT NOT NULL,
    start         datetime              NULL,
    end           datetime              NULL,
    type          VARCHAR(255)          NULL,
    `description` VARCHAR(255)          NULL,
    video         VARCHAR(255)          NULL,
    driver_id     BIGINT                NULL,
    CONSTRAINT pk_situations PRIMARY KEY (situation_id)
);

ALTER TABLE situations
    ADD CONSTRAINT FK_SITUATIONS_ON_DRIVER FOREIGN KEY (driver_id) REFERENCES drivers (user_id);


CREATE TABLE messages
(
    message_id  BIGINT AUTO_INCREMENT NOT NULL,
    time        datetime              NULL,
    text        VARCHAR(255)          NULL,
    sender_id   BIGINT                NULL,
    receiver_id BIGINT                NULL,
    CONSTRAINT pk_messages PRIMARY KEY (message_id)
);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES users (user_id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (user_id);
