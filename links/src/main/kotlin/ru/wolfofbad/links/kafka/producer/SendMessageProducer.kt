package ru.wolfofbad.links.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.links.configuration.KafkaConfiguration
import ru.wolfofbad.links.dto.authorization.SendMessageRequest

@Service
class SendMessageProducer(
    @Qualifier("messageKafkaTemplate")
    private val template: KafkaTemplate<String, SendMessageRequest>,

    config: KafkaConfiguration
) {
    private val topicName = config.messagesTopic.name

    fun sendMessage(messageRequest: SendMessageRequest) {
        template.send(topicName, messageRequest)
    }
}
