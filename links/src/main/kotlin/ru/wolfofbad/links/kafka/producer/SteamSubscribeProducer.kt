package ru.wolfofbad.links.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.links.configuration.KafkaConfiguration
import ru.wolfofbad.links.dto.steamservice.SteamSubscribeRequest
import java.net.URI

@Service
class SteamSubscribeProducer(
    @Qualifier("steamKafkaTemplate")
    private val template: KafkaTemplate<String, SteamSubscribeRequest>,

    config: KafkaConfiguration
) {
    private val topicName = config.steamServiceTopic.name

    fun subscribe(uri: URI) {
        template.send(topicName, SteamSubscribeRequest(uri, SteamSubscribeRequest.Type.SUBSCRIBE))
    }

    fun delete(uri: URI) {
        template.send(topicName, SteamSubscribeRequest(uri, SteamSubscribeRequest.Type.UNSUBSCRIBE))
    }
}
