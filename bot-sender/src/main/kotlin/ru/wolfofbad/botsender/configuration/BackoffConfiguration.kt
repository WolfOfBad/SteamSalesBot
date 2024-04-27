package ru.wolfofbad.botsender.configuration

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.boot.context.properties.bind.Name
import org.springframework.context.annotation.Bean
import ru.wolfofbad.botsender.retry.policy.BackoffPolicy
import ru.wolfofbad.botsender.retry.BackoffType
import ru.wolfofbad.botsender.retry.policy.ConstantBackoff
import ru.wolfofbad.botsender.retry.policy.ExponentialBackoff
import ru.wolfofbad.botsender.retry.policy.LinearBackoff

@ConfigurationProperties(prefix = "backoff", ignoreUnknownFields = false)
data class BackoffConfiguration(
    @NotNull
    @Name("telegram-bot")
    val telegramBot: BackoffConfig
) {
    @Bean("telegramBackoff")
    fun telegramBackoff(): BackoffPolicy {
        return when (telegramBot.type) {
            BackoffType.CONSTANT -> ConstantBackoff(telegramBot)
            BackoffType.LINEAR -> LinearBackoff(telegramBot)
            BackoffType.EXPONENTIAL -> ExponentialBackoff(telegramBot)
        }
    }

    data class BackoffConfig(
        @NotNull
        @DefaultValue("exponential")
        val type: BackoffType,
        @NotNull
        @Positive
        @DefaultValue("3")
        val attempts: Int,
        @NotNull
        @DefaultValue("1000")
        val waitTime: java.time.Duration
    )
}
