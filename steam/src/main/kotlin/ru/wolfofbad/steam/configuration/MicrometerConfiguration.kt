package ru.wolfofbad.steam.configuration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MicrometerConfiguration {
    @Bean
    fun linkSubscribeProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("steam_subscribes_processed")
    }

    @Bean
    fun linkUpdatesProcessedCounter(prometheusMeterRegistry: MeterRegistry): Counter {
        return prometheusMeterRegistry.counter("steam_updates_processed")
    }
}
