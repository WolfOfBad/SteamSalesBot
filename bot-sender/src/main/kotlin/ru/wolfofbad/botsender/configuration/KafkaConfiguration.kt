package ru.wolfofbad.botsender.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import ru.wolfofbad.botsender.dto.request.UpdateRequest


@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@EnableKafka
data class KafkaConfiguration(
    @NotNull
    @Name("messages-topic")
    val messagesTopic: TopicConfig
) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, UpdateRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesTopic.bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = messagesTopic.listenerId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaMessagesListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, UpdateRequest> {
        val factory =
            ConcurrentKafkaListenerContainerFactory<String, UpdateRequest>()
        factory.consumerFactory = consumerFactory()
        factory.setRecordMessageConverter(jsonConverter())
        return factory
    }

    @Bean
    fun jsonConverter(): StringJsonMessageConverter {
        return StringJsonMessageConverter(ObjectMapper())
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
