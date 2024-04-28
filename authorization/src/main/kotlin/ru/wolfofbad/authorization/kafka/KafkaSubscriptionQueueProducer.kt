package ru.wolfofbad.authorization.kafka

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.subscription.DeleteRequest
import ru.wolfofbad.authorization.dto.request.subscription.ListLinkRequest
import ru.wolfofbad.authorization.dto.request.subscription.SubscribeRequest
import java.net.URI

@Service
class KafkaSubscriptionQueueProducer(
    @Qualifier("authorizationKafkaTemplate")
    private val template: KafkaTemplate<String, Any>,
    config: KafkaConfiguration
) {
    private val topicName = config.subscriptionTopic.name

    fun delete(id: Long) {
        template.send(topicName, DeleteRequest(id))
    }

    fun subscribe(id: Long, uri: URI, type: SubscribeRequest.Type) {
        template.send(topicName, SubscribeRequest(id, uri, type))
    }

    fun listLinks(id: Long) {
        template.send(topicName, ListLinkRequest(id))
    }

}
