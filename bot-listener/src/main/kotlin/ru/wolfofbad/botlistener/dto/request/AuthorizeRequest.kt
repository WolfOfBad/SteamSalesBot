package ru.wolfofbad.botlistener.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthorizeRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long,
    @JsonProperty("type")
    val type: Type
) {
    enum class Type {
        START,
        RESET
    }
}
