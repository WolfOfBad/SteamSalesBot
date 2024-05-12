package ru.wolfofbad.botsender.configuration

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper
import org.springframework.kafka.support.serializer.JsonDeserializer
import ru.wolfofbad.botsender.dto.request.UpdateRequest


@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@EnableKafka
data class KafkaConfiguration(
    @NotNull
    @Name("messages-topic")
    val messagesTopic: TopicConfig,

    @NotNull
    @Name("messages_dlq-topic")
    val messagesDlqTopic: TopicConfig,
) {
    @Bean
    fun dlqMessagesProducerFactory(): ProducerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesDlqTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun dlqMessagesKafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(dlqMessagesProducerFactory())
    }

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
    fun jsonConverter(): RecordMessageConverter {
        val converter = StringJsonMessageConverter()
        val typeMapper = DefaultJackson2JavaTypeMapper()
        typeMapper.typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
        typeMapper.addTrustedPackages("*")

        val mappings = HashMap<String, Class<*>>()
        mappings["updateRequest"] = UpdateRequest::class.java
        typeMapper.idClassMapping = mappings

        converter.typeMapper = typeMapper
        return converter
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
