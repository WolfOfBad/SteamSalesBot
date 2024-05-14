/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.steam.domain.jooq.generated.tables.pojos


import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.io.Serializable

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
data class Link(
    var id: Long? = null,
    @get:NotNull
    @get:Size(max = 1000000000)
    var uri: String? = null
): Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: Link = other as Link
        if (this.id == null) {
            if (o.id != null)
                return false
        }
        else if (this.id != o.id)
            return false
        if (this.uri == null) {
            if (o.uri != null)
                return false
        }
        else if (this.uri != o.uri)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.id == null) 0 else this.id.hashCode())
        result = prime * result + (if (this.uri == null) 0 else this.uri.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("Link (")

        sb.append(id)
        sb.append(", ").append(uri)

        sb.append(")")
        return sb.toString()
    }
}
