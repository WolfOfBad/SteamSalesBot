package ru.wolfofbad.steam.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import java.time.Duration

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class ApplicationConfiguration(
    @Name("base-url")
    val baseUrl: String,
)
