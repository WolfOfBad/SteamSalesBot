package ru.wolfofbad.authorization.kafka

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.UpdateRequest

@Service
class KafkaMessageQueueProducer(
    @Qualifier("updatesKafkaTemplate")
    private val template: KafkaTemplate<String, UpdateRequest>,
    config: KafkaConfiguration
) {
    private val topicName = config.messagesTopic.name

    fun publish(tgChatId: Long, message: String) {
        template.send(topicName, UpdateRequest(tgChatId, message))
    }
}
