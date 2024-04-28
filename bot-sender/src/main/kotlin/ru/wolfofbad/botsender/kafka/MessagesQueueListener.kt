package ru.wolfofbad.botsender.kafka

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.botsender.bot.MessageSendService
import ru.wolfofbad.botsender.configuration.KafkaConfiguration
import ru.wolfofbad.botsender.dto.request.UpdateRequest

@Service
@KafkaListener(
    topics = ["\${kafka.messages-topic.name}"],
    containerFactory = "kafkaMessagesListenerContainerFactory",
    groupId = "\${kafka.messages-topic.listener-id}"
)
class MessagesQueueListener(
    private val messageSendService: MessageSendService,
    @Qualifier("dlqMessagesKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,
    config: KafkaConfiguration
) {
    private val logger = LogManager.getLogger(MessagesQueueListener::class.java)
    private val dlqTopicName = config.messagesTopic.name + "_dlq"

    @KafkaHandler
    fun handleMessages(request: UpdateRequest) {
        try {
            messageSendService.sendMessage(request)
        } catch (exception: Exception) {
            dlqTemplate.send(
                dlqTopicName,
                """{
                |   request: {
                |      $request
                |   }
                |   exception: {
                |      $exception
                |   }
                |}
            """
            )
        }
    }

    @KafkaHandler
    fun handleBadRequest(request: String) {
        logger.warn("Bad request: $request")
        dlqTemplate.send(
            dlqTopicName,
            """{
                |   bad-request: $request
                |}
            """
        )
    }
}
