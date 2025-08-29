create table users
(
    id         bigserial
        primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null
        unique,
    password   varchar(255) not null,
    team_id    integer
);

alter table users
    owner to seth;

create table user_roles
(
    user_id bigint  not null
        references users,
    role    varchar not null
);

alter table user_roles
    owner to seth;

create table teams
(
    id           bigserial
        primary key,
    name         varchar(255) not null,
    team_lead_id bigint
        references users
);

alter table teams
    owner to seth;

alter table users
    add constraint fk_users_team
        foreign key (team_id) references teams;

create table categories
(
    id              serial
        primary key,
    name            varchar(255) not null,
    default_team_id bigint       not null
        references teams
);

alter table categories
    owner to seth;

create table faqs
(
    id          bigserial
        primary key,
    question    text    not null,
    answer      text    not null,
    category_id integer not null
        references categories
);

alter table faqs
    owner to seth;

create table tickets
(
    id          bigserial
        primary key,
    title       varchar(255) not null,
    description text         not null,
    category_id integer      not null
        references categories,
    priority    varchar(50)
        constraint tickets_priority_check
            check ((priority IS NULL) OR ((priority)::text = ANY
                                          ((ARRAY ['LOW'::character varying, 'MEDIUM'::character varying, 'HIGH'::character varying])::text[]))),
    raised_by   bigint       not null
        references users,
    assigned_to bigint
        references users,
    status      varchar(50)  not null
        constraint tickets_status_check
            check ((status)::text = ANY
                   ((ARRAY ['PENDING'::character varying, 'OPEN'::character varying, 'RESOLVED'::character varying, 'CLOSED'::character varying])::text[])),
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    team_id     integer
        constraint fk_tickets_team
            references teams
);

alter table tickets
    owner to seth;

create table ticket_comments
(
    id         bigserial
        primary key,
    message    text      not null,
    author_id  bigint    not null
        references users,
    created_at timestamp not null,
    ticket_id  bigint    not null
        references tickets
);

alter table ticket_comments
    owner to seth;