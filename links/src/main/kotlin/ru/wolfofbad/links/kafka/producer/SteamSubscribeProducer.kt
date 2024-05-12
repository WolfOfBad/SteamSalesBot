package ru.wolfofbad.links.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.links.configuration.KafkaConfiguration
import ru.wolfofbad.links.dto.steamservice.DeleteRequest
import ru.wolfofbad.links.dto.steamservice.SubscribeRequest

@Service
class SteamSubscribeProducer(
    @Qualifier("steamKafkaTemplate")
    private val template: KafkaTemplate<String, Any>,

    config: KafkaConfiguration
) {
    private val topicName = config.steamServiceTopic.name

    fun subscribe(request: SubscribeRequest) {
        template.send(topicName, request)
    }

    fun delete(request: DeleteRequest) {
        template.send(topicName, request)
    }
}
