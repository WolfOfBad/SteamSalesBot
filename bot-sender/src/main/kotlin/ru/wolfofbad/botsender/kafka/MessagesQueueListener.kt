package ru.wolfofbad.botsender.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.wolfofbad.botsender.bot.MessageSendService
import ru.wolfofbad.botsender.dto.request.UpdateRequest

@Service
class MessagesQueueListener(
    private val messageSendService: MessageSendService
) {

    @KafkaListener(
        topics = ["\${kafka.messages-topic.name}"],
        containerFactory = "kafkaMessagesListenerContainerFactory",
        groupId = "\${kafka.messages-topic.listener-id}"
    )
    fun listenMessages(request: UpdateRequest) {
        messageSendService.sendMessage(request)
    }
}
