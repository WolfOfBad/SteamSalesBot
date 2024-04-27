package ru.wolfofbad.authorization.kafka

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.DeleteUserRequest
import ru.wolfofbad.authorization.dto.request.SubscribeRequest
import java.net.URI

@Service
class KafkaSubscriptionQueueProducer(
    @Qualifier("authorizationKafkaTemplate")
    private val template: KafkaTemplate<String, Any>,
    config: KafkaConfiguration
) {
    private val topicName = config.subscriptionTopic.name

    fun delete(id: Long) {
        template.send(topicName, DeleteUserRequest(id))
    }

    fun subscribe(id: Long, uri: URI, type: SubscribeRequest.Type) {
        template.send(topicName, SubscribeRequest(id, uri, type))
    }

}
