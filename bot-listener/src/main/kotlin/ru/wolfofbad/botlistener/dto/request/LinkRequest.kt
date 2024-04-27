package ru.wolfofbad.botlistener.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class LinkRequest(
    @JsonProperty("telegramChatId")
    val telegramChatId: Long,
    @JsonProperty("link")
    val link: URI,
    @JsonProperty("type")
    val type: Type
) {
    enum class Type {
        TRACK,
        UNTRACK
    }
}
