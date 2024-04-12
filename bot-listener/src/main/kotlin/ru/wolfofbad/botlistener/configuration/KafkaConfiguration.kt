package ru.wolfofbad.botlistener.configuration

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.wolfofbad.botlistener.dto.request.AuthorizeRequest
import ru.wolfofbad.botlistener.dto.request.UpdateRequest

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@EnableKafka
data class KafkaConfiguration(
    @NotNull
    @Name("messages-topic")
    val messagesTopic: TopicConfig,
    @NotNull
    @Name("authorization-topic")
    val authorizationTopic: TopicConfig,
) {
    @Bean
    fun updatesProducerFactory(): ProducerFactory<String, UpdateRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun updatesKafkaTemplate(): KafkaTemplate<String, UpdateRequest> {
        return KafkaTemplate(updatesProducerFactory())
    }

    @Bean
    fun authorizationProducerFactory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = authorizationTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.TYPE_MAPPINGS] = "authorize:  ru.wolfofbad.botlistener.dto.request.AuthorizeRequest, " +
            "link:ru.wolfofbad.botlistener.dto.request.LinkRequest, " +
            "list-link:ru.wolfofbad.botlistener.dto.request.ListLinkRequest"

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun authorizationKafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(authorizationProducerFactory())
    }

    class TopicConfig(
        @NotEmpty
        @NotNull
        val name: String,
        @NotNull
        @NotEmpty
        val listenerId: String,
        @NotNull
        @NotEmpty
        val bootstrapAddress: String
    )
}
