package ru.wolfofbad.botlistener.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.configuration.KafkaConfiguration
import ru.wolfofbad.botlistener.dto.request.AuthorizeRequest
import ru.wolfofbad.botlistener.dto.request.LinkRequest
import ru.wolfofbad.botlistener.dto.request.ListLinkRequest

@Service
class AuthorizationQueueProducer(
    @Qualifier("authorizationKafkaTemplate")
    val template: KafkaTemplate<String, Any>,

    configuration: KafkaConfiguration
) {
    val topicName = configuration.authorizationTopic.name

    fun sendAuthorizationRequest(authorizeRequest: AuthorizeRequest) {
        template.send(topicName, authorizeRequest)
    }

    fun sendLinkRequest(linkRequest: LinkRequest) {
        template.send(topicName, linkRequest)
    }

    fun sendListLinkRequest(listLinkRequest: ListLinkRequest) {
        template.send(topicName, listLinkRequest)
    }
}
