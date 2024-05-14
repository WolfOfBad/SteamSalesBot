package ru.wolfofbad.steam.retry.impl

import ru.wolfofbad.steam.retry.BackoffPolicy
import kotlin.time.Duration

class ConstantBackoff(
    private val waitTime: Duration
) : BackoffPolicy {

    override fun getWaitTime(attempt: Int): Duration {
        return waitTime
    }
}
