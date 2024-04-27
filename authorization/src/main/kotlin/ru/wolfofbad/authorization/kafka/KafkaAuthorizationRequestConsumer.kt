package ru.wolfofbad.authorization.kafka

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.authorization.configuration.KafkaConfiguration
import ru.wolfofbad.authorization.dto.request.AuthorizeRequest
import ru.wolfofbad.authorization.dto.request.LinkRequest
import ru.wolfofbad.authorization.dto.request.ListLinkRequest

@Service
@KafkaListener(
    groupId = "\${kafka.authorization-topic.listener-id}",
    containerFactory = "authorizeKafkaListenerContainerFactory",
    topics = ["\${kafka.authorization-topic.name}"],
)
class KafkaAuthorizationRequestConsumer(
    @Qualifier("dlqAuthorizationKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,
    config: KafkaConfiguration
) {
    private val logger = LogManager.getLogger(KafkaAuthorizationRequestConsumer::class.java)
    private val dlqTopicName = config.authorizationTopic.name + "_dlq"

    @KafkaHandler
    fun handleAuthorizationRequest(request: AuthorizeRequest) {
        try {
            logger.info(request.toString())
        } catch (exception: Exception) {
            sendExceptionToDlq(request.toString(), exception)
        }
    }

    @KafkaHandler
    fun handleLinkRequest(request: LinkRequest) {
        try {
            logger.info(request.toString())
        } catch (exception: Exception) {
            sendExceptionToDlq(request.toString(), exception)
        }
    }

    @KafkaHandler
    fun handleListLinkRequest(request: ListLinkRequest) {
        try {
            logger.info(request.toString())
        } catch (exception: Exception) {
            sendExceptionToDlq(request.toString(), exception)
        }
    }

    @KafkaHandler(isDefault = true)
    fun handleBadRequest(request: String) {
        logger.warn(request)
        dlqTemplate.send(
            dlqTopicName, """{
            |   bad-request: $request
            |}
        """.trimMargin()
        )
    }

    private fun sendExceptionToDlq(request: String, exception: Exception) {
        dlqTemplate.send(
            dlqTopicName, """{
            |   request: {
            |       $request
            |   }
            |   exception: {
            |       $exception
            |   }
            |}
        """.trimMargin()
        )
    }
}
