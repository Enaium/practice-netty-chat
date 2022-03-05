create table `group`
(
    id     bigint auto_increment
        primary key,
    name   varchar(255)              not null,
    member varchar(255) default '[]' not null
);

create table user
(
    id              bigint auto_increment
        primary key,
    username        varchar(255) not null,
    password        varchar(255) not null,
    create_datetime datetime     not null
);

create table user_info
(
    id       bigint                                  not null
        primary key,
    nickname varchar(255) default 'Default Username' null,
    friend   varchar(255) default '[]'               null,
    `group`  varchar(255) default '[]'               not null
);


