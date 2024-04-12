package ru.wolfofbad.botlistener.dto.request

data class AuthorizeRequest(
    val telegramChatId: Long,
    val type: Type
) {
    enum class Type {
        START,
        RESET
    }
}
