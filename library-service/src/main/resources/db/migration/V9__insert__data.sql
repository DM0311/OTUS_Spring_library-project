INSERT INTO genres (name)
VALUES ('Роман'),
       ('Научная фантастика'),
       ('Детектив'),
       ('Фэнтези'),
       ('Биография'),
       ('Классика'),
       ('Приключения'),
       ('Исторический роман');

INSERT INTO authors (full_name)
VALUES ('Лев Толстой'),
       ('Фёдор Достоевский'),
       ('Джордж Оруэлл'),
       ('Агата Кристи'),
       ('Дж.Р.Р. Толкин'),
       ('Михаил Булгаков'),
       ('Артур Конан Дойл'),
       ('Рэй Брэдбери');

INSERT INTO users (email, username, full_name, password, is_blocked, blocked_until, penalty_points, created_at)
VALUES ('admin@library.com', 'admin', 'Администратор', '$2a$10$/hXyNAcQGXiRkYp1ARIlqeHsp9ve79fh8vxQN9nACZbFdvILFMXzq',
        false, NULL, 0, CURRENT_TIMESTAMP),
       ('ivanov@mail.ru', 'ivanov', 'Иван Иванов', '$2a$10$/hXyNAcQGXiRkYp1ARIlqeHsp9ve79fh8vxQN9nACZbFdvILFMXzq',
        false, NULL, 0, CURRENT_TIMESTAMP),
       ('petrov@mail.ru', 'petrov', 'Пётр Петров', '$2a$10$/hXyNAcQGXiRkYp1ARIlqeHsp9ve79fh8vxQN9nACZbFdvILFMXzq', true,
        CURRENT_TIMESTAMP + INTERVAL '7' DAY, 5, CURRENT_TIMESTAMP),
       ('sidorova@mail.ru', 'sidorova', 'Анна Сидорова', '$2a$10$/hXyNAcQGXiRkYp1ARIlqeHsp9ve79fh8vxQN9nACZbFdvILFMXzq',
        false, NULL, 2, CURRENT_TIMESTAMP);

INSERT INTO user_roles (user_id, role_name)
VALUES (1, 'ADMIN'),
       (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER');

INSERT INTO books (title, publication_year, description, total_copies, available_copies, rating, created_at)
VALUES ('Война и мир', '1869',
        'Великий роман-эпопея о жизни русского общества в эпоху наполеоновских войн. Сложные судьбы, любовь, война и философские размышления.',
        5, 5, 4.8, CURRENT_TIMESTAMP),
       ('Преступление и наказание', '1866',
        'Психологический роман о студенте Раскольникове, который решается на убийство, пытаясь проверить свою теорию о сверхчеловеке.',
        4, 3, 4.7, CURRENT_TIMESTAMP),
       ('1984', '1949',
        'Классическая антиутопия о тоталитарном обществе, где правят Большой Брат и Полиция Мыслей. Роман о свободе, правде и сопротивлении.',
        3, 2, 4.5, CURRENT_TIMESTAMP),
       ('Убийство в Восточном экспрессе', '1934',
        'Знаменитый детектив Эркюля Пуаро расследует загадочное убийство в поезде, следующем из Стамбула в Париж.', 4,
        4, 4.3, CURRENT_TIMESTAMP),
       ('Властелин колец: Братство кольца', '1954',
        'Начало великой эпопеи о кольце всевластья и его хранителе Фродо, отправляющемся в опасное путешествие в Мордор.',
        6, 5, 4.9, CURRENT_TIMESTAMP),
       ('Мастер и Маргарита', '1967',
        'Мистический роман, где сатира на советскую действительность переплетается с библейской историей Иешуа Га-Ноцри и любовью Мастера и Маргариты.',
        4, 4, 4.9, CURRENT_TIMESTAMP),
       ('Шерлок Холмс: Собака Баскервилей', '1902',
        'Знаменитое расследование Шерлока Холмса о таинственной собаке-призраке, убивающей членов семьи Баскервилей.',
        3, 3, 4.4, CURRENT_TIMESTAMP),
       ('451 градус по Фаренгейту', '1953',
        'Антиутопия о будущем, где книги запрещены и сжигаются пожарными. Бунт одного человека против системы и ценность знаний.',
        3, 2, 4.2, CURRENT_TIMESTAMP);

INSERT INTO books_authors (book_id, author_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8);

INSERT INTO books_genres (book_id, genre_id)
VALUES (1, 1),
       (1, 5),
       (2, 1),
       (2, 5),
       (3, 2),
       (3, 5),
       (4, 3),
       (4, 1),
       (5, 4),
       (5, 6),
       (6, 1),
       (6, 4),
       (7, 3),
       (7, 5),
       (8, 2),
       (8, 5);