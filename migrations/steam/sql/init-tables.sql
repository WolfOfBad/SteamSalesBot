create table if not exists link
(
    id  bigint generated always as identity,

    uri text not null,

    primary key (id),
    unique (uri)
);
create index on link(uri)
