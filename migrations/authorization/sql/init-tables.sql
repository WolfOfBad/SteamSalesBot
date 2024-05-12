create table if not exists chat
(
    id         bigint generated always as identity,
    tg_chat_id bigint not null,

    primary key (id),
    unique (tg_chat_id)
);
create index tg_chat_id_index on chat (tg_chat_id);
