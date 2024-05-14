create table if not exists chat
(
    id bigint not null,

    primary key (id)
);

create table if not exists link
(
    id  bigint generated always as identity,
    uri text not null,

    primary key (id),
    unique (uri)
);
create index uri_index on link (uri);

create table if not exists chat_link
(
    chat_id     bigint references chat (id) not null,
    link_id     bigint references link (id) not null,

    last_update timestamp with time zone    not null,

    unique (chat_id, link_id)
)
