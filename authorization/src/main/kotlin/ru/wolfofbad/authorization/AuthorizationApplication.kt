package ru.wolfofbad.authorization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.wolfofbad.authorization.configuration.KafkaConfiguration

@SpringBootApplication
@EnableConfigurationProperties(KafkaConfiguration::class)
class AuthorizationApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationApplication>(*args)
}
