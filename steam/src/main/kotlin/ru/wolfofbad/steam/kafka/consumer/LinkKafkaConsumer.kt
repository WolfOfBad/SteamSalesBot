package ru.wolfofbad.steam.kafka.consumer

import io.micrometer.core.instrument.Counter
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.steam.configuration.KafkaConfiguration
import ru.wolfofbad.steam.dto.links.SteamSubscribeRequest
import ru.wolfofbad.steam.service.LinkService

@Service
@KafkaListener(
    groupId = "\${kafka.steam-topic.listener-id}",
    containerFactory = "linkKafkaListenerContainerFactory",
    topics = ["\${kafka.steam-topic.name}"]
)
class LinkKafkaConsumer(
    @Qualifier("dlqLinkKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,

    private val linkService: LinkService,

    @Qualifier("linkSubscribeProcessedCounter")
    private val counter: Counter,

    config: KafkaConfiguration
) {
    private val dlqTopicName = config.steamDlqTopic.name
    private val logger = LogManager.getLogger(this::class.java)

    @KafkaHandler
    fun handleSubscribeRequest(request: SteamSubscribeRequest) {
        try {
            when (request.type) {
                SteamSubscribeRequest.Type.SUBSCRIBE -> linkService.add(request.uri)
                SteamSubscribeRequest.Type.UNSUBSCRIBE -> linkService.delete(request.uri)
            }
        } catch (e: Exception) {
            sendExceptionToDlq(request.toString(), e)
        } finally {
            counter.increment()
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
        counter.increment()
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
