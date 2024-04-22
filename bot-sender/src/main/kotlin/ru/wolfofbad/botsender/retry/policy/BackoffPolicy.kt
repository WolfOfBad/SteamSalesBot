package ru.wolfofbad.botlistener.retry.policy

import kotlin.time.Duration

interface BackoffPolicy {
    fun getWaitTime(attempt : Int) : Duration

    fun getMaxAttempts() : Int
}
