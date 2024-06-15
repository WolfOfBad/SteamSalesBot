package ru.wolfofbad.links.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.boot.context.properties.bind.Name
import java.time.Duration

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class ApplicationConfiguration(
    @Name("send-interval")
    @DefaultValue("2h")
    val sendInterval: Duration
)
