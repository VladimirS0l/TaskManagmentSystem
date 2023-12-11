create table if not exists users
(
    id bigserial primary key,
    username varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    created_date timestamp default current_timestamp,
    updated_date timestamp
);

create table if not exists roles
(
    id serial primary key,
    role varchar(255)
);

create table if not exists users_roles
(
    user_id bigint not null references users(id) on delete cascade,
    roles_id int not null references roles(id) on delete cascade,
    primary key (user_id, roles_id)
);

create table if not exists tasks
(
    id bigserial primary key,
    title varchar(255) not null,
    description text not null,
    status varchar(255) not null,
    priority varchar(255) not null,
    author_id bigint not null references users(id) on update no action,
    assignee varchar(255),
    created_date timestamp default current_timestamp,
    updated_date timestamp
);

create table if not exists comments
(
    id bigserial primary key,
    task_id bigint not null references tasks(id) on update no action,
    username varchar(255) not null,
    message text not null,
    created_date timestamp default current_timestamp,
);
