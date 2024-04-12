package ru.wolfofbad.botlistener.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long,
    @JsonProperty("message")
    val message: String
) {
}
