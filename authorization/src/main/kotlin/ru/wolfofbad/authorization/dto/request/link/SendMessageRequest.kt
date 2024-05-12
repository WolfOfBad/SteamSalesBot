package ru.wolfofbad.authorization.dto.request.link

import com.fasterxml.jackson.annotation.JsonProperty

data class SendMessageRequest(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("message")
    val message: String
)
