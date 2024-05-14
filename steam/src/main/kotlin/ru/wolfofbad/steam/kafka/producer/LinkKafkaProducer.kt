package ru.wolfofbad.steam.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.steam.configuration.KafkaConfiguration
import ru.wolfofbad.steam.dto.links.UpdateRequest

@Service
class LinkKafkaProducer(
    @Qualifier("linkKafkaTemplate")
    private val template: KafkaTemplate<String, UpdateRequest>,

    config: KafkaConfiguration
) {
    private val topicName = config.linksTopic.name

    fun publish(request: UpdateRequest) {
        template.send(topicName, request)
    }
}
