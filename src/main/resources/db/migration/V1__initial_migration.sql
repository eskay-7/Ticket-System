create table users (
   id bigserial not null primary key,
   first_name varchar(255) not null,
   last_name varchar(255) not null,
   email varchar(255) not null unique,
   password varchar(255) not null
);

create table user_roles (
    user_id bigint not null,
    role varchar not null,
    foreign key(user_id) references users(id)
);