/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.links.domain.jooq.generated.tables.pojos


import jakarta.validation.constraints.NotNull

import java.io.Serializable
import java.time.OffsetDateTime

import javax.annotation.processing.Generated


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = [
        "https://www.jooq.org",
        "jOOQ version:3.18.13"
    ],
    comments = "This class is generated by jOOQ"
)
@Suppress("UNCHECKED_CAST")
data class ChatLink(
    @get:NotNull
    var chatId: Long? = null,
    @get:NotNull
    var linkId: Long? = null,
    @get:NotNull
    var lastUpdate: OffsetDateTime? = null
): Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: ChatLink = other as ChatLink
        if (this.chatId == null) {
            if (o.chatId != null)
                return false
        }
        else if (this.chatId != o.chatId)
            return false
        if (this.linkId == null) {
            if (o.linkId != null)
                return false
        }
        else if (this.linkId != o.linkId)
            return false
        if (this.lastUpdate == null) {
            if (o.lastUpdate != null)
                return false
        }
        else if (this.lastUpdate != o.lastUpdate)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.chatId == null) 0 else this.chatId.hashCode())
        result = prime * result + (if (this.linkId == null) 0 else this.linkId.hashCode())
        result = prime * result + (if (this.lastUpdate == null) 0 else this.lastUpdate.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("ChatLink (")

        sb.append(chatId)
        sb.append(", ").append(linkId)
        sb.append(", ").append(lastUpdate)

        sb.append(")")
        return sb.toString()
    }
}
