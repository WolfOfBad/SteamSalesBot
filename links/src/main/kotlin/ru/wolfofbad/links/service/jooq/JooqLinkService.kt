package ru.wolfofbad.links.service.jooq

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.wolfofbad.links.domain.ChatRepository
import ru.wolfofbad.links.domain.LinkRepository
import ru.wolfofbad.links.dto.authorization.SendMessageRequest
import ru.wolfofbad.links.dto.steamservice.DeleteRequest
import ru.wolfofbad.links.dto.steamservice.SubscribeRequest
import ru.wolfofbad.links.exception.AlreadySubscribedException
import ru.wolfofbad.links.exception.NoLinksException
import ru.wolfofbad.links.exception.NotSubscribedException
import ru.wolfofbad.links.kafka.producer.SendMessageProducer
import ru.wolfofbad.links.kafka.producer.SteamSubscribeProducer
import ru.wolfofbad.links.service.LinkService
import java.net.URI

@Service
class JooqLinkService(
    private val chatRepository: ChatRepository,
    private val linkRepository: LinkRepository,

    private val messageProducer: SendMessageProducer,
    private val steamProducer: SteamSubscribeProducer
) : LinkService {
    @Transactional
    override fun track(id: Long, uri: URI) {
        val chat = chatRepository.findById(id) ?: chatRepository.addUser(id)
        val link = linkRepository.findByUri(uri) ?: linkRepository.add(uri)

        try {
            linkRepository.subscribe(link, chat)
        } catch (e: DuplicateKeyException) {
            throw AlreadySubscribedException()
        }

        messageProducer.sendMessage(
            SendMessageRequest(
                id,
                "Вы подписались на товар. Если в магазине появится скидка на него, бот сообщит вам об этом"
            )
        )

        steamProducer.subscribe(SubscribeRequest(uri))
    }

    @Transactional
    override fun untrack(id: Long, uri: URI) {
        val chat = chatRepository.findById(id) ?: throw NotSubscribedException()
        val link = linkRepository.findByUri(uri) ?: throw NotSubscribedException()

        try {
            linkRepository.unsubscribe(link, chat)
        } catch (e: EmptyResultDataAccessException) {
            throw NotSubscribedException()
        } catch (e: NullPointerException) {
            throw NotSubscribedException()
        }

        if (linkRepository.getAllByChatId(id).isEmpty()) {
            chatRepository.deleteUser(id)
        }

        if (linkRepository.getUsers(uri).isEmpty()) {
            linkRepository.delete(uri)

            steamProducer.delete(DeleteRequest(uri))
        }

        messageProducer.sendMessage(
            SendMessageRequest(
                id,
                "Вы отписались от товара. Теперь вам не будут приходить уведомления о появляющихся на него скидках"
            )
        )
    }

    @Transactional
    override fun listLink(id: Long) {
        val chat = chatRepository.findById(id) ?: throw NoLinksException()
        val links = linkRepository.getAllByChatId(chat.id)

        if (links.isEmpty()) {
            throw NoLinksException()
        } else {
            messageProducer.sendMessage(
                SendMessageRequest(
                    id,
                    "Список отслеживаемых ссылок:\n\n"
                        + links.joinToString(separator = "\n") { it.uri.toString() }
                )
            )
        }
    }

    @Transactional
    override fun deleteUser(id: Long) {
        val chat = chatRepository.findById(id) ?: return

        val subscriptions = linkRepository.getAllByChatId(id)
        for (subscription in subscriptions) {
            linkRepository.unsubscribe(subscription, chat)

            if (linkRepository.getUsers(subscription.uri).isEmpty()) {
                linkRepository.delete(subscription.uri)
                steamProducer.delete(DeleteRequest(subscription.uri))
            }
        }

        chatRepository.deleteUser(id)
    }
}
