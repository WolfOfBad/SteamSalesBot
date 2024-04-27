package ru.wolfofbad.botsender.retry.policy

import kotlin.time.Duration

interface BackoffPolicy {
    fun getWaitTime(attempt : Int) : Duration

    fun getMaxAttempts() : Int
}
