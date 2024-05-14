package ru.wolfofbad.steam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.wolfofbad.steam.configuration.ApplicationConfiguration
import ru.wolfofbad.steam.configuration.BackoffConfiguration
import ru.wolfofbad.steam.configuration.KafkaConfiguration
import ru.wolfofbad.steam.configuration.SchedulerConfiguration

@SpringBootApplication
@EnableConfigurationProperties(
    KafkaConfiguration::class,
    BackoffConfiguration::class,
    ApplicationConfiguration::class,
    SchedulerConfiguration::class
)
class SteamApplication

fun main(args: Array<String>) {
    runApplication<SteamApplication>(*args)
}
