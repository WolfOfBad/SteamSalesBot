package ru.wolfofbad.steam.retry.impl

import ru.wolfofbad.steam.retry.BackoffPolicy
import kotlin.math.pow
import kotlin.time.Duration

class ExponentialBackoff(
    private val waitTime: Duration
) : BackoffPolicy {
    private val factor: Double = 2.0

    override fun getWaitTime(attempt: Int): Duration {
        return waitTime.times(factor.pow(attempt))
    }
}
