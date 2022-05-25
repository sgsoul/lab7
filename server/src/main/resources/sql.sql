create table organization
(
    id serial,
    name varchar not null,
    coordinates_id int not null,
    "creationDate" timestamp not null,
    "annualTurnover" real not null,
    "fullName" varchar,
    "employeesCount" bigint,
    "organizationType_id" int,
    address_id int not null,
    user_id int not null
);

create unique index organization_id_uindex
    on organization (id);

create table coordinates
(
    id serial
        constraint coordinates_pk
            primary key,
    x real not null,
    y real
);

create table "organizationType"
(
    id serial
        constraint organizationtype_pk
            primary key,
    name varchar not null
);

create table address
(
    id serial
        constraint address_pk
            primary key,
    street varchar not null,
    "zipCode" varchar not null,
    location_id int not null
);

create table location
(
    id serial
        constraint location_pk
            primary key,
    x bigint not null,
    y real not null,
    z bigint not null,
    name varchar
);

create table "user"
(
    id serial
        constraint user_pk
            primary key,
    login varchar not null,
    password varchar not null
);

INSERT INTO "organizationType" (id, name) VALUES (DEFAULT, 'PUBLIC');

INSERT INTO "organizationType" (id, name) VALUES (DEFAULT, 'GOVERNMENT');

INSERT INTO "organizationType" (id, name) VALUES (DEFAULT, 'TRUST');

INSERT INTO "organizationType" (id, name) VALUES (DEFAULT, 'OPEN_JOIN_STOCK_COMPANY');
