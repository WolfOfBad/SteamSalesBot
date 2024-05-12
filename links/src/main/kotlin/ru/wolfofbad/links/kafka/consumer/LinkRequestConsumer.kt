package ru.wolfofbad.links.kafka.consumer

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.links.configuration.KafkaConfiguration
import ru.wolfofbad.links.dto.authorization.DeleteRequest
import ru.wolfofbad.links.dto.authorization.ListLinkRequest
import ru.wolfofbad.links.dto.authorization.SendMessageRequest
import ru.wolfofbad.links.dto.authorization.SubscribeRequest
import ru.wolfofbad.links.exception.SendMessageException
import ru.wolfofbad.links.kafka.producer.SendMessageProducer
import ru.wolfofbad.links.service.LinkService

@Service
@KafkaListener(
    groupId = "\${kafka.link-topic.listener-id}",
    containerFactory = "linkKafkaListenerContainerFactory",
    topics = ["\${kafka.link-topic.name}"],
)
class LinkRequestConsumer(
    @Qualifier("dlqLinkKafkaTemplate")
    private val dlqTemplate: KafkaTemplate<String, String>,

    private val linkService: LinkService,
    private val sendMessageService: SendMessageProducer,

    config: KafkaConfiguration
) {
    private val dlqTopicName = config.linkDlqTopic.name
    private val logger = LogManager.getLogger(LinkRequestConsumer::class.java)

    @KafkaHandler
    fun handleSubscribeRequest(request: SubscribeRequest) {
        try {
            when (request.type) {
                SubscribeRequest.Type.TRACK -> linkService.track(request.id, request.uri)
                SubscribeRequest.Type.UNTRACK -> linkService.untrack(request.id, request.uri)
            }
        } catch (e: SendMessageException) {
            sendMessageService.sendMessage(
                SendMessageRequest(
                    request.id,
                    e.getTelegramMessage()
                )
            )
        } catch (e: Exception) {
            logger.error(e.message)
            sendExceptionToDlq(request.toString(), e)
        }
    }

    @KafkaHandler
    fun handleDeleteRequest(request: DeleteRequest) {
        try {
            linkService.deleteUser(request.id)
        } catch (e: SendMessageException) {
            sendMessageService.sendMessage(
                SendMessageRequest(
                    request.id,
                    e.getTelegramMessage()
                )
            )
        } catch (e: Exception) {
            logger.error(e.message)
            sendExceptionToDlq(request.toString(), e)
        }
    }

    @KafkaHandler
    fun handleListLinkRequest(request: ListLinkRequest) {
        try {
            linkService.listLink(request.id)
        } catch (e: SendMessageException) {
            sendMessageService.sendMessage(
                SendMessageRequest(
                    request.id,
                    e.getTelegramMessage()
                )
            )
        } catch (e: Exception) {
            logger.error(e.message)
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
