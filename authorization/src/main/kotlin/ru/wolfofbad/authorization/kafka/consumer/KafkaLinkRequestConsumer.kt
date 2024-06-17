package ru.wolfofbad.authorization.kafka.consumer

import io.micrometer.core.instrument.Counter
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.link.SendMessageRequest
import ru.wolfofbad.authorization.service.SendMessageService

@Service
@KafkaListener(
    groupId = "\${kafka.authorization-messages-topic.listener-id}",
    containerFactory = "authorizationLinkKafkaListenerContainerFactory",
    topics = ["\${kafka.authorization-messages-topic.name}"],
)
class KafkaLinkRequestConsumer(
    @Qualifier("dlqAuthorizationLinkKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,

    private val messageService: SendMessageService,

    @Qualifier("linkAuthorizationsProcessedCounter")
    private val counter: Counter,

    config: KafkaConfiguration
) {
    private val dlqTopicName = config.authorizationMessagesDlqTopic.name
    private val logger = LogManager.getLogger(this.javaClass)

    @KafkaHandler
    fun handleMessage(request: SendMessageRequest) {
        try {
            messageService.sendMessage(request)
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
