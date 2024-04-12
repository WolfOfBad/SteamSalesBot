package ru.wolfofbad.botlistener.dto.request

import java.net.URI

data class LinkRequest(
    val telegramChatId: Long,
    val link: URI,
    val type: Type
) {
    enum class Type {
        TRACK,
        UNTRACK
    }
}
