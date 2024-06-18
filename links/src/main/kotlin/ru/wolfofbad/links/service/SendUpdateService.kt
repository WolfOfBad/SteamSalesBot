package ru.wolfofbad.links.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.wolfofbad.links.configuration.ApplicationConfiguration
import ru.wolfofbad.links.domain.LinkRepository
import ru.wolfofbad.links.dto.authorization.SendMessageRequest
import ru.wolfofbad.links.dto.steamservice.UpdateRequest
import ru.wolfofbad.links.kafka.producer.SendMessageProducer

@Service
class SendUpdateService(
    private val sendMessageProducer: SendMessageProducer,

    private val linkRepository: LinkRepository,

    config: ApplicationConfiguration
) {
    private val interval = config.sendInterval

    @Transactional
    fun sendUpdate(request: UpdateRequest) {
        val link = linkRepository.findByUri(request.uri) ?: throw RuntimeException("Link ${request.uri} not found")

        val users = linkRepository.getUsersToUpdate(link, interval)
        for (user in users) {
            sendMessageProducer.sendMessage(
                SendMessageRequest(
                    user.id,
                    "Обновление по ссылке: ${link.uri}\n\n" +
                        "Предыдущая цена: ${request.initialFormatted}\nТекущая цена: ${request.finalFormatted}\n" +
                        "Скидка: ${request.discount}%"
                )
            )
        }
    }
}
