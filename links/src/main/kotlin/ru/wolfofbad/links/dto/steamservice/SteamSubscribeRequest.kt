package ru.wolfofbad.links.dto.steamservice

import java.net.URI

data class SteamSubscribeRequest(
    val uri: URI,

    val type: Type
) {
    enum class Type {
        SUBSCRIBE,
        UNSUBSCRIBE
    }
}
