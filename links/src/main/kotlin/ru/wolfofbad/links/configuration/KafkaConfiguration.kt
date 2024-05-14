package ru.wolfofbad.links.configuration

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
import ru.wolfofbad.links.dto.authorization.DeleteRequest
import ru.wolfofbad.links.dto.authorization.ListLinkRequest
import ru.wolfofbad.links.dto.authorization.SendMessageRequest
import ru.wolfofbad.links.dto.authorization.SubscribeRequest
import ru.wolfofbad.links.dto.steamservice.SteamSubscribeRequest
import ru.wolfofbad.links.dto.steamservice.UpdateRequest

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
data class KafkaConfiguration(
    @NotNull
    @Name("authorization-messages-topic")
    val messagesTopic: TopicConfig,

    @NotNull
    @Name("link-topic")
    val linkTopic: TopicConfig,

    @NotNull
    @Name("link_dlq-topic")
    val linkDlqTopic: TopicConfig,

    @NotNull
    @Name("steam-service-topic")
    val steamServiceTopic: TopicConfig,

    @NotNull
    @Name("link-update-topic")
    val linkUpdateTopic: TopicConfig,

    @NotNull
    @Name("link-update_dlq-topic")
    val linkUpdateDlqTopic: TopicConfig,
) {
    @Bean
    fun dlqLinkProducerFactory(): ProducerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = linkDlqTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun dlqLinkKafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(dlqLinkProducerFactory())
    }

    @Bean
    fun linkKafkaConsumerFactory(): ConsumerFactory<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = linkTopic.bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = linkTopic.listenerId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun linkKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = linkKafkaConsumerFactory()
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
        mappings["deleteUserRequest"] = DeleteRequest::class.java
        mappings["subscribeRequest"] = SubscribeRequest::class.java
        mappings["listLinkRequest"] = ListLinkRequest::class.java
        typeMapper.idClassMapping = mappings

        converter.typeMapper = typeMapper
        return converter
    }

    @Bean
    fun messageProducerFactory(): ProducerFactory<String, SendMessageRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.TYPE_MAPPINGS] = "sendMessage:ru.wolfofbad.links.dto.authorization.SendMessageRequest"

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun messageKafkaTemplate(): KafkaTemplate<String, SendMessageRequest> {
        return KafkaTemplate(messageProducerFactory())
    }

    @Bean
    fun steamProducerFactory(): ProducerFactory<String, SteamSubscribeRequest> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = messagesTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.TYPE_MAPPINGS] =
            "subscribeRequest:ru.wolfofbad.links.dto.steamservice.SteamSubscribeRequest"

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun steamKafkaTemplate(): KafkaTemplate<String, SteamSubscribeRequest> {
        return KafkaTemplate(steamProducerFactory())
    }

    @Bean
    fun steamKafkaConsumerFactory(): ConsumerFactory<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = linkUpdateTopic.bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = linkUpdateTopic.listenerId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun steamKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = steamKafkaConsumerFactory()
        factory.setRecordMessageConverter(steamJsonConverter())

        return factory
    }

    @Bean
    fun steamJsonConverter(): RecordMessageConverter {
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

    @Bean
    fun dlqSteamProducerFactory(): ProducerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = linkUpdateDlqTopic.bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun dlqSteamKafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(dlqLinkProducerFactory())
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
