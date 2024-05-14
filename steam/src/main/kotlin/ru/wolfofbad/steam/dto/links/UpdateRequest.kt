package ru.wolfofbad.steam.dto.links

import java.net.URI

data class UpdateRequest(
    val uri: URI,

    val oldValue: Int,
    val newValue: Int,

    val currency: String,

    val discount: Int,

    val initialFormatted: String,
    val finalFormatted: String,
) {
}
