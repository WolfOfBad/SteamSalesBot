package ru.wolfofbad.links.domain.dto

import java.net.URI

data class Link(
    val id: Long,
    val uri: URI
) {
}
