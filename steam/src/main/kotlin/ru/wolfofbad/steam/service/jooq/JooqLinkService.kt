package ru.wolfofbad.steam.service.jooq

import org.springframework.stereotype.Service
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

    private val updateProducer: LinkKafkaProducer
): LinkService {
    private val regex = "^https://store.steampowered.com/app/(\\d+)/.*$".toRegex()

    override fun add(uri: URI) {
        repository.add(uri)
    }

    override fun delete(uri: URI) {
        repository.delete(uri)
    }

    override fun checkUpdates() {
        val links = repository.getAll()

        for (link in links) {
            val info = steamClient.getGameInfo(getId(link))
                .data
                .priceInfo

            if (info.discount != 0) {
                updateProducer.publish(UpdateRequest(
                    link.uri,
                    info.initial,
                    info.final,
                    info.currency,
                    info.discount,
                    info.initialFormated,
                    info.finalFormated
                ))
            }
        }
    }

    private fun getId(link: Link): Long {
        val match = regex.find(link.uri.toString())

        return match?.groups?.get(1)?.value?.toLong() ?: throw Exception("Cannot parse uri")
    }
}
