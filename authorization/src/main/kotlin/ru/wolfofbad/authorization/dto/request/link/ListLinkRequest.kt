package ru.wolfofbad.authorization.dto.request.link

import com.fasterxml.jackson.annotation.JsonProperty

data class ListLinkRequest(
    @JsonProperty("id")
    val id: Long
) {
}
