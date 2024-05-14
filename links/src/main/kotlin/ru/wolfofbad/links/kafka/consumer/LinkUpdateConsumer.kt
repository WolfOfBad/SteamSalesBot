package ru.wolfofbad.links.kafka.consumer

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.links.configuration.KafkaConfiguration
import ru.wolfofbad.links.dto.steamservice.UpdateRequest
import ru.wolfofbad.links.service.SendUpdateService

@Service
@KafkaListener(
    groupId = "\${kafka.link-update-topic.listener-id}",
    containerFactory = "steamKafkaListenerContainerFactory",
    topics = ["\${kafka.link-update-topic.name}"],
)
class LinkUpdateConsumer(
    @Qualifier("dlqSteamKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,

    private val updateService: SendUpdateService,

    config: KafkaConfiguration
) {
    private val dlqTopicName = config.linkUpdateDlqTopic.name
    private val logger = LogManager.getLogger(LinkRequestConsumer::class.java)

    @KafkaHandler
    fun handleUpdate(request: UpdateRequest) {
        try {
            updateService.sendUpdate(request)
        } catch (e: Exception) {
            sendExceptionToDlq(request.toString(), e)
        }
    }

    @KafkaHandler(isDefault = true)
    fun handleBadRequest(request: String) {
        logger.error(request)
        dlqTemplate.send(
            dlqTopicName, """{
            |   bad-request: $request
            |}
        """
        )
    }

    private fun sendExceptionToDlq(request: String, exception: Exception) {
        dlqTemplate.send(
            dlqTopicName, """{
            |   request: {
            |       $request
            |   },
            |   exception: {
            |       ${exception.message}
            |   },
            |   stacktrace: {
            |       ${exception.stackTrace}
            |   }
            |}
        """
        )
    }
}
