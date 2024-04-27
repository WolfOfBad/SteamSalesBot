package ru.wolfofbad.authorization.service

interface ChatService {
    fun register(tgChatId: Long)

    fun unregister(tgChatId: Long)
}
