package ru.wolfofbad.botsender.configuration

import com.pengrad.telegrambot.TelegramBot
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean

@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
data class BotConfiguration(
    @NotEmpty
    @NotNull
    val token: String
) {
    @Bean
    fun telegramBot(): TelegramBot {
        return TelegramBot(token)
    }
}
