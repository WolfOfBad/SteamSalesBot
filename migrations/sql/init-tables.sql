create table if not exists chat
(
    id         bigint generated always as identity,
    tg_chat_id bigint not null,

    primary key (id),
    unique (tg_chat_id)
);
create index tg_chat_id_index on chat (tg_chat_id);

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
    chat_id bigint not null,
    link_id bigint not null,

    unique (chat_id, link_id)
)
