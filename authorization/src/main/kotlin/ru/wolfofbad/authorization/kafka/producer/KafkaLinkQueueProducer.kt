package ru.wolfofbad.authorization.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.link.DeleteRequest
import ru.wolfofbad.authorization.dto.request.link.ListLinkRequest
import ru.wolfofbad.authorization.dto.request.link.SubscribeRequest
import java.net.URI

@Service
class KafkaLinkQueueProducer(
    @Qualifier("linkKafkaTemplate")
    private val template: KafkaTemplate<String, Any>,
    config: KafkaConfiguration
) {
    private val topicName = config.linkTopic.name

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
