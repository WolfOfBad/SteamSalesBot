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
import ru.wolfofbad.authorization.dto.request.UpdateRequest
import ru.wolfofbad.authorization.exception.SendMessageException
import ru.wolfofbad.authorization.service.ChatService

@Service
@KafkaListener(
    groupId = "\${kafka.authorization-topic.listener-id}",
    containerFactory = "authorizeKafkaListenerContainerFactory",
    topics = ["\${kafka.authorization-topic.name}"],
)
class KafkaAuthorizationRequestConsumer(
    @Qualifier("dlqAuthorizationKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,
    @Qualifier("updatesKafkaTemplate")
    private val updatesTemplate: KafkaTemplate<String, UpdateRequest>,

    private val chatService: ChatService,
    config: KafkaConfiguration
) {
    private val logger = LogManager.getLogger(KafkaAuthorizationRequestConsumer::class.java)
    private val updatesTopicName = config.messagesTopic.name
    private val dlqTopicName = config.authorizationDlqTopic.name

    @KafkaHandler
    fun handleAuthorizationRequest(request: AuthorizeRequest) {
        try {
            when (request.type) {
                AuthorizeRequest.Type.START -> chatService.register(request.telegramChatId)
                AuthorizeRequest.Type.RESET -> chatService.unregister(request.telegramChatId)
            }
        } catch (exception: SendMessageException) {
            updatesTemplate.send(
                updatesTopicName,
                UpdateRequest(request.telegramChatId, exception.getTelegramMessage())
            )
        } catch (exception: Exception) {
            logger.error("Error while sending authorization request", exception)
            sendExceptionToDlq(request.toString(), exception)
        }
    }

    @KafkaHandler
    fun handleLinkRequest(request: LinkRequest) {
        try {
            TODO()
            logger.info(request.toString())
        } catch (exception: Exception) {
            sendExceptionToDlq(request.toString(), exception)
        }
    }

    @KafkaHandler
    fun handleListLinkRequest(request: ListLinkRequest) {
        try {
            TODO()
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
