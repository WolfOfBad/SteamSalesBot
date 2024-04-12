package ru.wolfofbad.botlistener.kafka

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.configuration.KafkaConfiguration
import ru.wolfofbad.botlistener.dto.request.UpdateRequest

@Service
class MessagesQueueProducer(
    @Qualifier("updatesKafkaTemplate")
    private val template: KafkaTemplate<String, UpdateRequest>,

    configuration: KafkaConfiguration
) {
    private val topicName = configuration.messagesTopic.name

    fun sendRequest(updateRequest: UpdateRequest) {
        template.send(topicName, updateRequest)
    }
}
