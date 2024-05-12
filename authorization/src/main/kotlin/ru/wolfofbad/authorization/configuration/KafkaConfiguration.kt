package ru.wolfofbad.authorization.configuration

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.wolfofbad.authorization.dto.request.bot.AuthorizeRequest
import ru.wolfofbad.authorization.dto.request.bot.LinkRequest
import ru.wolfofbad.authorization.dto.request.bot.ListLinkRequest
import ru.wolfofbad.authorization.dto.request.bot.UpdateRequest
import ru.wolfofbad.authorization.dto.request.link.SendMessageRequest

@ConfigurationProperties(prefix = "kafka")
data class KafkaConfiguration(
    @NotNull
    @Name("authorization-topic")
    val authorizationTopic: TopicConfig,

    @NotNull
    @Name("authorization_dlq-topic")
    val authorizationDlqTopic: TopicConfig,

    @NotNull
    @Name("messages-topic")
    val messagesTopic: TopicConfig,

    @NotNull
    @Name("link-topic")
    val linkTopic: TopicConfig,

    @NotNull
    @Name("authorization-messages-topic")
    val authorizationMessagesTopic: TopicConfig,

    @NotNull
    @Name("authorization-messages_dlq-topic")
    val authorizationMessagesDlqTopic: TopicConfig,
) {
    @Bean
    fun dlqAuthorizationProducerFactory(): ProducerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = authorizationDlqTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun dlqAuthorizationKafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(dlqAuthorizationProducerFactory())
    }

    @Bean
    fun authorizeKafkaConsumerFactory(): ConsumerFactory<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = authorizationTopic.bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = authorizationTopic.listenerId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun authorizeKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = authorizeKafkaConsumerFactory()
        factory.setRecordMessageConverter(authorizeJsonConverter())

        return factory
    }

    @Bean
    fun authorizeJsonConverter(): RecordMessageConverter {
        val converter = StringJsonMessageConverter()
        val typeMapper = DefaultJackson2JavaTypeMapper()
        typeMapper.typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
        typeMapper.addTrustedPackages("*")

        val mappings = HashMap<String, Class<*>>()
        mappings["authorizeRequest"] = AuthorizeRequest::class.java
        mappings["linkRequest"] = LinkRequest::class.java
        mappings["listLinkRequest"] = ListLinkRequest::class.java
        typeMapper.idClassMapping = mappings

        converter.typeMapper = typeMapper
        return converter
    }

    @Bean
    fun updatesProducerFactory(): ProducerFactory<String, UpdateRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.TYPE_MAPPINGS] = "updateRequest:ru.wolfofbad.authorization.dto.request.bot.UpdateRequest"

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun updatesKafkaTemplate(): KafkaTemplate<String, UpdateRequest> {
        return KafkaTemplate(updatesProducerFactory())
    }

    @Bean
    fun linkProducerFactory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = linkTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.TYPE_MAPPINGS] =
            "deleteUserRequest:ru.wolfofbad.authorization.dto.request.link.DeleteRequest, " +
                "subscribeRequest:ru.wolfofbad.authorization.dto.request.link.SubscribeRequest, " +
                "listLinkRequest:ru.wolfofbad.authorization.dto.request.link.ListLinkRequest"

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun linkKafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(linkProducerFactory())
    }

    @Bean
    fun authorizationLinkKafkaConsumerFactory(): ConsumerFactory<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = authorizationMessagesTopic.bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = authorizationMessagesTopic.listenerId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun authorizationLinkKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = authorizationLinkKafkaConsumerFactory()
        factory.setRecordMessageConverter(linkJsonConverter())

        return factory
    }

    @Bean
    fun linkJsonConverter(): RecordMessageConverter {
        val converter = StringJsonMessageConverter()
        val typeMapper = DefaultJackson2JavaTypeMapper()
        typeMapper.typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
        typeMapper.addTrustedPackages("*")

        val mappings = HashMap<String, Class<*>>()
        mappings["sendMessage"] = SendMessageRequest::class.java
        typeMapper.idClassMapping = mappings

        converter.typeMapper = typeMapper
        return converter
    }

    @Bean
    fun dlqAuthorizationLinkProducerFactory(): ProducerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = authorizationMessagesDlqTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun dlqAuthorizationLinkKafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(dlqAuthorizationLinkProducerFactory())
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
