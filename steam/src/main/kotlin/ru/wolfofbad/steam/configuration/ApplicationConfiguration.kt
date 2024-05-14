package ru.wolfofbad.steam.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.Name

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
class ApplicationConfiguration(
    @Name("base-url")
    val baseUrl: String
) {
}
