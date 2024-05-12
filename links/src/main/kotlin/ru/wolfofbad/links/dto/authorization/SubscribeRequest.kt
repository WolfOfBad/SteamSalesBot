package ru.wolfofbad.links.dto.authorization

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class SubscribeRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("uri")
    val uri: URI,
    @JsonProperty("type")
    val type: Type
) {
    enum class Type {
        TRACK,
        UNTRACK
    }
}
