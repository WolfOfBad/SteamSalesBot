package ru.wolfofbad.steam.dto.steam

import com.fasterxml.jackson.annotation.JsonProperty

data class Game(
    @JsonProperty("success")
    val success: Boolean,

    @JsonProperty("data")
    val data: GameInfo
) {
}
