package ru.wolfofbad.authorization.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteUserRequest(
    @JsonProperty("id")
    val id: Long
) {
}
