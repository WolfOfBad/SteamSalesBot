package ru.wolfofbad.links

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.wolfofbad.links.configuration.ApplicationConfiguration
import ru.wolfofbad.links.configuration.KafkaConfiguration

@SpringBootApplication
@EnableConfigurationProperties(KafkaConfiguration::class, ApplicationConfiguration::class)
class LinksApplication

fun main(args: Array<String>) {
    runApplication<LinksApplication>(*args)
}
