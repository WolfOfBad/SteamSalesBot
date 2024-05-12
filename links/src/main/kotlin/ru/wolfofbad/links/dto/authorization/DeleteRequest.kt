package ru.wolfofbad.links.dto.authorization

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteRequest(
    @JsonProperty("id")
    val id: Long
) {
}
