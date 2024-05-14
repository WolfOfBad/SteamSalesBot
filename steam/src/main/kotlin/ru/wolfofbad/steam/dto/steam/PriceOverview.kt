package ru.wolfofbad.steam.dto.steam

import com.fasterxml.jackson.annotation.JsonProperty

data class PriceOverview(
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("initial")
    val initial: Int,
    @JsonProperty("final")
    val final: Int,
    @JsonProperty("discount_percent")
    val discount: Int,

    @JsonProperty("initial_formatted")
    val initialFormated: String,
    @JsonProperty("final_formatted")
    val finalFormated: String
) {
}
