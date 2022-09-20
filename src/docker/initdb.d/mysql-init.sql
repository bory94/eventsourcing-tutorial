CREATE TABLE event_source
(
    uuid         varchar(36)  not null,
    type         varchar(100) not null,
    aggregate_id varchar(36),
    version      int          not null default 1,
    payload      TEXT,
    created_at   timestamp    not null default current_timestamp,

    primary key (uuid)
);

create index idx_event_source_created_at on event_source (created_at);
create index idx_event_source_aggregate_id on event_source (aggregate_id);

CREATE TABLE client
(
    uuid         varchar(36)  not null,
    name         varchar(100) not null,
    phone_number varchar(20)  not null,
    address      varchar(100) not null,
    deleted      bit          not null default false,
    version      int          not null default 1,
    created_at   timestamp    not null default current_timestamp,
    updated_at   timestamp    not null default current_timestamp,

    primary key (uuid)
);

create index idx_client_created_at on client (created_at);
create index idx_client_updated_at on client (updated_at);

CREATE TABLE project
(
    uuid        varchar(36)   not null,
    name        varchar(256)  not null,
    description varchar(4000) not null,
    created_at  timestamp     not null default current_timestamp,
    updated_at  timestamp     not null default current_timestamp,

    client_uuid varchar(36)   not null,

    primary key (uuid),
    foreign key (client_uuid) references client (uuid)
);

create index idx_project_name on project (name);
create index idx_project_created_at on project (created_at);
create index idx_project_updated_at on project (updated_at);