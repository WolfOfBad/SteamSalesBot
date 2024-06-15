package ru.wolfofbad.steam.service.jooq

import org.springframework.stereotype.Service
import ru.wolfofbad.steam.configuration.ApplicationConfiguration
import ru.wolfofbad.steam.domain.LinkRepository
import ru.wolfofbad.steam.domain.dto.Link
import ru.wolfofbad.steam.dto.links.UpdateRequest
import ru.wolfofbad.steam.kafka.producer.LinkKafkaProducer
import ru.wolfofbad.steam.service.LinkService
import ru.wolfofbad.steam.service.SteamClient
import java.net.URI

@Service
class JooqLinkService(
    private val repository: LinkRepository,

    private val steamClient: SteamClient,

    private val updateProducer: LinkKafkaProducer,
) : LinkService {

    override fun add(uri: URI) {
        repository.add(uri)
    }

    override fun delete(uri: URI) {
        repository.delete(uri)
    }

    override fun checkUpdates() {
        val links = repository.getAll()
        if (links.isNotEmpty()) {
            val games = steamClient.getGamesInfo(links)

            for (game in games) {
                val info = game.value
                    .data
                    .priceInfo

                if (info.discount != 0) {
                    updateProducer.publish(
                        UpdateRequest(
                            game.key.uri,
                            info.initial,
                            info.final,
                            info.currency,
                            info.discount,
                            info.initialFormated,
                            info.finalFormated
                        )
                    )
                }
            }
        }
    }
}
