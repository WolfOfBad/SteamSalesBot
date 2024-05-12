package ru.wolfofbad.authorization.service

import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.dto.request.link.SendMessageRequest
import ru.wolfofbad.authorization.kafka.producer.KafkaMessageQueueProducer

@Service
class SendMessageService(
    private val chatService: ChatService,

    private val messageProducer: KafkaMessageQueueProducer
) {
    fun sendMessage(request: SendMessageRequest) {
        val chat = chatService.getById(request.id) ?: throw RuntimeException("Can't find ${request.id} id")

        messageProducer.publish(
            chat.tgChatId,
            request.message
        )
    }
}
