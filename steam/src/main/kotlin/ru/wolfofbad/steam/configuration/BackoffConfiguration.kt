package ru.wolfofbad.steam.configuration

import jakarta.validation.constraints.PositiveOrZero
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import ru.wolfofbad.steam.retry.BackoffPolicy
import ru.wolfofbad.steam.retry.BackoffType
import ru.wolfofbad.steam.retry.impl.ConstantBackoff
import ru.wolfofbad.steam.retry.impl.ExponentialBackoff
import ru.wolfofbad.steam.retry.impl.LinearBackoff
import java.time.Duration
import kotlin.time.toKotlinDuration

@ConfigurationProperties(prefix = "backoff", ignoreUnknownFields = false)
class BackoffConfiguration(
    @DefaultValue("constant")
    val type: BackoffType,

    @DefaultValue("0")
    @PositiveOrZero
    val maxAttempts: Int,

    @DefaultValue("1s")
    val waitTime: Duration,

    @DefaultValue("")
    val codes: List<HttpStatus>
) {
    @Bean
    fun backoffPolicy(): BackoffPolicy {
        return when (type) {
            BackoffType.CONSTANT -> ConstantBackoff(waitTime.toKotlinDuration())
            BackoffType.LINEAR -> LinearBackoff(waitTime.toKotlinDuration())
            BackoffType.EXPONENTIAL -> ExponentialBackoff(waitTime.toKotlinDuration())
        }
    }
}
