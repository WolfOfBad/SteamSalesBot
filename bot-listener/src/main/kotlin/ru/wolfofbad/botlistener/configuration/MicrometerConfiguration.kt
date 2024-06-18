package ru.wolfofbad.botlistener.configuration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MicrometerConfiguration {
    @Bean
    fun messagesProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("bot-listener_messages_processed")
    }
}
