package ru.wolfofbad.authorization.configuration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MicrometerConfiguration {
    @Bean
    fun telegramAuthorizationsProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("authorization_telegram_proceed_total")
    }

    @Bean
    fun linkAuthorizationsProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("authorization_link_proceed_total")
    }
}
