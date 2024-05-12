package ru.wolfofbad.botlistener.kafka.producer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.configuration.KafkaConfiguration
import ru.wolfofbad.botlistener.dto.request.UpdateRequest
import java.lang.Exception

@Service
class MessagesQueueProducer(
    @Qualifier("updatesKafkaTemplate")
    private val template: KafkaTemplate<String, UpdateRequest>,
    @Qualifier("messagesDlqKafkaTemplate")
    private val templateDlq: KafkaTemplate<String, String>,

    configuration: KafkaConfiguration
) {
    private val topicName = configuration.messagesTopic.name
    private val dlqTopicName = configuration.messagesTopic.name

    fun sendRequest(updateRequest: UpdateRequest) {
        template.send(topicName, updateRequest)
    }

    fun sendDlqMessage(exception: Exception) {
        templateDlq.send(dlqTopicName, """{
            |   listener-exception: {
            |       message: {
            |           ${exception.message}
            |       },
            |       stacktrace: {
            |           ${exception.stackTrace}
            |       }
            |   }
            |}
        """)
    }
}
