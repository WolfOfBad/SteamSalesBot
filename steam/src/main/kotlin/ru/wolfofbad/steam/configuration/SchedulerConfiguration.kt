package ru.wolfofbad.steam.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.Duration

@ConfigurationProperties(prefix = "scheduler", ignoreUnknownFields = false)
@EnableScheduling
data class SchedulerConfiguration(
    @DefaultValue("30m")
    val interval: Duration
) {
    @Bean
    fun schedulerInterval() = interval
}
