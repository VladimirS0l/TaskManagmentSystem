create table if not exists users
(
    id bigserial primary key,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    created_date timestamp default current_timestamp,
    updated_date timestamp
);

create table if not exists roles
(
    id bigserial primary key,
    role varchar(255)
);

create table if not exists users_roles
(
    user_id bigserial not null,
    role_id bigserial not null,
    primary key (user_id, role_id),
    constraint fk_users_roles_users foreign key (user_id) references users(id) on update no action,
    constraint fk_users_roles_roles foreign key (role_id) references roles(id) on update no action
);

create table if not exists tasks
(
    id bigserial primary key,
    title varchar(255) not null,
    description text not null,
    status varchar(255) not null,
    priority varchar(255) not null,
    author_id bigserial not null,
    performer_id bigserial not null,
    created_date timestamp default current_timestamp,
    updated_date timestamp,
    constraint fk_users_tasks_authors foreign key (author_id) references users(id) on update no action,
    constraint fk_users_tasks_performers foreign key (performer_id) references users(id) on update no action
);

create table if not exists comments
(
    id bigserial primary key,
    task_comment_id bigserial not null,
    user_comment_id bigserial not null,
    message text not null,
    created_date timestamp default current_timestamp,
    updated_date timestamp,
    constraint fk_tasks_comments_task_id foreign key (task_comment_id) references tasks(id) on update no action,
    constraint fk_users_comments_user_id foreign key (user_comment_id) references users(id) on update no action
);
