package ru.wolfofbad.botsender.retry.policy

import ru.wolfofbad.botsender.configuration.BackoffConfiguration
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

class ExponentialBackoff(configuration: BackoffConfiguration.BackoffConfig) : BackoffPolicy {
    private val waitTime: java.time.Duration = configuration.waitTime
    private val attempts: Int = configuration.attempts

    override fun getWaitTime(attempt: Int): Duration {
        return waitTime.toKotlinDuration().times(FACTOR.pow(attempt.toDouble()))
    }

    override fun getMaxAttempts(): Int {
        return attempts
    }
}

private const val FACTOR: Double = 2.0
