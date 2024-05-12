package ru.wolfofbad.links.dto.authorization

import com.fasterxml.jackson.annotation.JsonProperty

data class ListLinkRequest(
    @JsonProperty("id")
    val id: Long
) {
}
