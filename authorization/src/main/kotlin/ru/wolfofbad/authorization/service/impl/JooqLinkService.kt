package ru.wolfofbad.authorization.service.impl

import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.domain.ChatRepository
import ru.wolfofbad.authorization.dto.request.bot.LinkRequest
import ru.wolfofbad.authorization.dto.request.bot.ListLinkRequest
import ru.wolfofbad.authorization.dto.request.subscription.SubscribeRequest
import ru.wolfofbad.authorization.exception.NoSuchChatException
import ru.wolfofbad.authorization.kafka.KafkaSubscriptionQueueProducer
import ru.wolfofbad.authorization.service.LinkService

@Service
class JooqLinkService(
    private val repository: ChatRepository,
    private val producer: KafkaSubscriptionQueueProducer
): LinkService {
    override fun handleLink(request: LinkRequest) {
        val chat = repository.getByTgChatId(request.telegramChatId) ?: throw NoSuchChatException()

        producer.subscribe(
            chat.id,
            request.link,
            when (request.type) {
                LinkRequest.Type.TRACK -> SubscribeRequest.Type.TRACK
                LinkRequest.Type.UNTRACK -> SubscribeRequest.Type.UNTRACK
            }
        )
    }

    override fun getLinks(request: ListLinkRequest) {
        val chat = repository.getByTgChatId(request.telegramChatId) ?: throw NoSuchChatException()

        producer.listLinks(chat.id)
    }
}
