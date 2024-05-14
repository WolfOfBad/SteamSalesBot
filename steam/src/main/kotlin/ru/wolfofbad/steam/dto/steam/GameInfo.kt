package ru.wolfofbad.steam.dto.steam

import com.fasterxml.jackson.annotation.JsonProperty

data class GameInfo(
    @JsonProperty("price_overview")
    val priceInfo: PriceOverview
) {
}
