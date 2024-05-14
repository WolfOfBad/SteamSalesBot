package ru.wolfofbad.steam.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.wolfofbad.steam.configuration.ApplicationConfiguration
import ru.wolfofbad.steam.dto.steam.Game
import ru.wolfofbad.steam.retry.RetryExchangeFilter

@Service
class SteamClient(
    retryFilter: RetryExchangeFilter,

    config: ApplicationConfiguration
) {
    private val webClient = WebClient.builder()
        .baseUrl(config.baseUrl)
        .filter(retryFilter)
        .build()
    private val mapper = jacksonObjectMapper()

    fun getGameInfo(gameId: Long): Game {
        val response = webClient.get().uri { uriBuilder ->
            uriBuilder.path("/api/appdetails/")
                .queryParam("filters", "price_overview")
                .queryParam("appids", gameId)
                .build()
        }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()!!

        val map = mapper.readValue<Map<String, Any>>(response)

        return mapper.convertValue(map[gameId.toString()], Game::class.java)
    }

}
