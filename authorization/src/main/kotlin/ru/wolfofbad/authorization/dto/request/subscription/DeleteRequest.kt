package ru.wolfofbad.authorization.dto.request.subscription

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteRequest(
    @JsonProperty("id")
    val id: Long
) {
}
