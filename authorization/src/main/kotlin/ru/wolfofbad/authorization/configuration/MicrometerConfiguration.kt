package ru.wolfofbad.authorization.configuration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MicrometerConfiguration {
    @Bean
    fun telegramAuthorizationsProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("telegram_authorizations_processed")
    }

    @Bean
    fun linkAuthorizationsProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("link_authorizations_processed")
    }
}
