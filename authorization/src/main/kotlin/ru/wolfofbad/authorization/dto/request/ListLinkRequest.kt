package ru.wolfofbad.authorization.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ListLinkRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long
)
