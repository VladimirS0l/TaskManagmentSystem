insert into users (username, email, password) values
                                                  ('Ivan Ivanov', 'test@mail.ru', 'MTIzMTIz'),
                                                  ('Petr Petrov', 'test2@mail.ru', 'NDU2NDU2');

insert into roles (role) values
                             ('ROLE_USER'),
                             ('ROLE_ADMIN');

insert into users_roles (user_id, roles_id)
values (1, 1),
       (2, 1),
       (2, 2);

insert into tasks (title, description, status, priority, author_id, assignee)
values ('Test title 1', 'Description 1', 'TODO', 'HIGH', 1,'Petr Petrov'),
       ('Test title 2', 'Description 2', 'TODO', 'HIGH', 1, 'Ivan Ivanov'),
       ('Test title 3', 'Description 3', 'DONE', 'MIDDLE', 2, 'Petr Petrov'),
       ('Test title 4', 'Description 4', 'IN_PROGRESS', 'LOW', 1, 'Petr Petrov');

insert into comments (task_id, username, message)
values (1, 'Petr Petrov', 'Message 1'),
       (1, 'Ivan Ivanov', 'Message 2'),
       (1, 'Petr Petrov', 'Message 3'),
       (2, 'Ivan Ivanov', 'Message 4'),
       (3, 'Petr Petrov', 'Message 5'),
       (4, 'Ivan Ivanov', 'Message 6'),
       (4, 'Petr Petrov', 'Message 7'),
       (3, 'Petr Petrov', 'Message 8');

