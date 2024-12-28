
-- Видалення існуючих таблиць, якщо вони є
DROP TABLE IF EXISTS group_songs;
DROP TABLE IF EXISTS genre_groups;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS songs;
DROP TABLE IF EXISTS genres;

-- Створення таблиці жанрів
CREATE TABLE genres
(
    id          SERIAL PRIMARY KEY, -- Використання SERIAL для автоматичної генерації ID
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

-- Створення таблиці пісень
CREATE TABLE songs
(
    id       SERIAL PRIMARY KEY, -- Використання SERIAL для автоматичної генерації ID
    name     TEXT    NOT NULL,
    duration INTEGER NOT NULL,
    CONSTRAINT duration_positive CHECK (duration > 0)
);

-- Створення таблиці груп
CREATE TABLE groups
(
    id            SERIAL PRIMARY KEY, -- Використання SERIAL для автоматичної генерації ID
    name          VARCHAR(50) NOT NULL,
    members_count INTEGER     NOT NULL
);

-- Таблиця користувачів
CREATE TABLE users
(
    id                      SERIAL PRIMARY KEY,
    username                VARCHAR(50) UNIQUE NOT NULL,
    password                VARCHAR(255)       NOT NULL,
    role                    VARCHAR(50)        NOT NULL DEFAULT 'ROLE_USER',
    enabled                 BOOLEAN                     DEFAULT TRUE,
    account_non_expired     BOOLEAN                     DEFAULT TRUE,
    account_non_locked      BOOLEAN                     DEFAULT TRUE,
    credentials_non_expired BOOLEAN                     DEFAULT TRUE,
    created_at              TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

-- Додавання прикладних користувачів
INSERT INTO users (username, password, role)
VALUES ('admin', '{noop}123', 'ROLE_ADMIN'),
       ('user', '{noop}123', 'ROLE_USER');


-- Додавання даних у таблицю songs
INSERT INTO songs (name, duration)
VALUES ('А що?', 355),
       ('Кульбаби', 390),
       ('Навіки', 233),
       ('Сніг', 480),
       ('Щедра ніч', 200),
       ('Я вільна', 183);

-- Додавання даних у таблицю genres
INSERT INTO genres (name, description)
VALUES ('Диско', 'Музика з ритмічними секціями баса та ударних'),
       ('Класична музика', 'Вона поширилися в XVIII столітті'),
       ('Поп', 'Один із сучасних музичних стилів, розрахований на широку публіку');

-- Додавання даних у таблицю groups
INSERT INTO groups (name, members_count)
VALUES ('Антитіла', 4),
       ('Гайдамаки', 5),
       ('Океан Ельзи', 6);


