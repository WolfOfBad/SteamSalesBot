package ru.wolfofbad.botsender

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.wolfofbad.botsender.configuration.BackoffConfiguration
import ru.wolfofbad.botsender.configuration.BotConfiguration
import ru.wolfofbad.botsender.configuration.KafkaConfiguration

@SpringBootApplication
@EnableConfigurationProperties(KafkaConfiguration::class, BackoffConfiguration::class, BotConfiguration::class)
class BotSenderApplication

fun main(args: Array<String>) {
    runApplication<BotSenderApplication>(*args)
}
