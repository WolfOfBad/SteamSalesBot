package ru.wolfofbad.steam.dto.links

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class SteamSubscribeRequest(
    @JsonProperty("uri")
    val uri: URI,

    @JsonProperty("type")
    val type: Type
) {
    enum class Type {
        SUBSCRIBE,
        UNSUBSCRIBE
    }
}
