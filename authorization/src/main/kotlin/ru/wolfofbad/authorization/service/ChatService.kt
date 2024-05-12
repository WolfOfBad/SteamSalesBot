package ru.wolfofbad.authorization.service

import ru.wolfofbad.authorization.dto.TgChat

interface ChatService {
    fun register(tgChatId: Long)

    fun unregister(tgChatId: Long)

    fun getById(id: Long): TgChat?
}
