package ru.wolfofbad.links.dto.steamservice

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class UpdateRequest(
    @JsonProperty("uri")
    val uri: URI,

    @JsonProperty("oldValue")
    val oldValue: Int,
    @JsonProperty("newValue")
    val newValue: Int,

    @JsonProperty("currency")
    val currency: String,

    @JsonProperty("discount")
    val discount: Int,

    @JsonProperty("initialFormatted")
    val initialFormatted: String,
    @JsonProperty("finalFormatted")
    val finalFormatted: String,
) {
}
