package ru.wolfofbad.authorization.dto.request.bot

import com.fasterxml.jackson.annotation.JsonProperty

data class ListLinkRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long
)
