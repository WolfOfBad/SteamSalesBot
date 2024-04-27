package ru.wolfofbad.authorization.service.impl

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.wolfofbad.authorization.domain.ChatRepository
import ru.wolfofbad.authorization.exception.AlreadyRegisteredException
import ru.wolfofbad.authorization.exception.NoSuchChatException
import ru.wolfofbad.authorization.kafka.KafkaMessageQueueProducer
import ru.wolfofbad.authorization.kafka.KafkaSubscriptionQueueProducer
import ru.wolfofbad.authorization.service.ChatService

@Service
class JooqChatService(
    private val chatRepository: ChatRepository,
    private val subscriptionProducer: KafkaSubscriptionQueueProducer,
    private val messageProducer: KafkaMessageQueueProducer
) : ChatService {

    @Transactional
    override fun register(tgChatId: Long) {
        try {
            chatRepository.add(tgChatId)

            messageProducer.publish(
                tgChatId,
                "Вы успешно запустили бота. Теперь вы можете отслеживать ссылки. " +
                    "Чтобы узнать больше, используйте команду /help"
            )
        } catch (exception: DuplicateKeyException) {
            throw AlreadyRegisteredException()
        }
    }

    @Transactional
    override fun unregister(tgChatId: Long) {
        try {
            chatRepository.removeByTgChatId(tgChatId)

            subscriptionProducer.delete(tgChatId)
            messageProducer.publish(
                tgChatId,
                "Вы удалили все записи из бота. Чтобы снова начать работу с ботом, введите /start"
            )
        } catch (exception: Exception) {
            when (exception) {
                is EmptyResultDataAccessException, is NoSuchElementException -> throw NoSuchChatException()
                else -> throw exception
            }
        }
    }
}
