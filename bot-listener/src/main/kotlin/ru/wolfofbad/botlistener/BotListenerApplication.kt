package ru.wolfofbad.botlistener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.wolfofbad.botlistener.configuration.BotConfiguration
import ru.wolfofbad.botlistener.configuration.KafkaConfiguration

@SpringBootApplication
@EnableConfigurationProperties(BotConfiguration::class, KafkaConfiguration::class)
class BotListenerApplication

fun main(args: Array<String>) {
    runApplication<BotListenerApplication>(*args)
}
