package ru.wolfofbad.authorization.domain

import ru.wolfofbad.authorization.dto.TgChat

interface ChatRepository {
    fun getByTgChatId(tgChatId: Long): TgChat?

    fun getById(id: Long): TgChat?

    fun add(tgChatId: Long): TgChat

    fun removeByTgChatId(tgChatId: Long): TgChat
}
