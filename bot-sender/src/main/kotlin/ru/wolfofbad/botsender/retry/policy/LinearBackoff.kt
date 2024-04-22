package ru.wolfofbad.botsender.retry.policy

import ru.wolfofbad.botlistener.retry.policy.BackoffPolicy
import ru.wolfofbad.botsender.configuration.BackoffConfiguration
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

class LinearBackoff(configuration: BackoffConfiguration.BackoffConfig) : BackoffPolicy {
    private val waitTime: java.time.Duration = configuration.waitTime
    private val attempts: Int = configuration.attempts

    override fun getWaitTime(attempt: Int): Duration {
        return waitTime.toKotlinDuration().times(attempt)
    }

    override fun getMaxAttempts(): Int {
        return attempts
    }
}

