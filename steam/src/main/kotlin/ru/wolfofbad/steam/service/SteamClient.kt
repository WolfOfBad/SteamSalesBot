package ru.wolfofbad.steam.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.wolfofbad.steam.configuration.ApplicationConfiguration
import ru.wolfofbad.steam.domain.dto.Link
import ru.wolfofbad.steam.dto.steam.Game
import ru.wolfofbad.steam.retry.RetryExchangeFilter
import java.util.stream.Collectors

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
    private val baseUrl = config.baseUrl
    private val regex = "^$baseUrl/app/(\\d+)/.*$".toRegex()

    fun getGamesInfo(links: List<Link>): Map<Link, Game> {
        val ids = links.stream()
            .collect(Collectors.toMap({ link -> getId(link).toString() }, { link -> link }))

        val queryIds = ids.entries.stream()
            .map { id -> id.key }
            .reduce("") { a, b -> "$a,$b" }
            .substring(1)

        val response = webClient.get().uri { uriBuilder ->
            uriBuilder.path("/api/appdetails/")
                .queryParam("filters", "price_overview")
                .queryParam("appids", queryIds)
                .build()
        }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()!!

        val map = HashMap<Link, Game>()
        for (game in mapper.readValue<Map<String, Any>>(response).entries) {
            var deserializedGame: Game?
            try {
                deserializedGame = mapper.convertValue(game.value, Game::class.java)
            } catch (e: Exception) {
                continue
            }

            map[ids[game.key] ?: throw Exception("No link found in response")] = deserializedGame
        }

        return map
    }

    private fun getId(link: Link): Long {
        val match = regex.find(link.uri.toString())

        return match?.groups?.get(1)?.value?.toLong() ?: throw Exception("Cannot parse uri")
    }
}
