package ru.wolfofbad.steam.retry

import kotlin.time.Duration

fun interface BackoffPolicy {
    fun getWaitTime(attempt: Int): Duration
}
