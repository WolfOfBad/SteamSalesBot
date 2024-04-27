package ru.wolfofbad.botlistener.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ListLinkRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long
)
