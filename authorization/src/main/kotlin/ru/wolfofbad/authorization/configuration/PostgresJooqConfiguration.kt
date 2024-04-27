package ru.wolfofbad.authorization.configuration

import org.jooq.conf.RenderQuotedNames
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class PostgresJooqConfiguration {
    @Bean
    fun postgresJooqCustomizer(): DefaultConfigurationCustomizer {
        return DefaultConfigurationCustomizer { c: DefaultConfiguration ->
            c.settings()
                .withRenderSchema(false)
                .withRenderFormatted(true)
                .withRenderQuotedNames(RenderQuotedNames.NEVER)
        }
    }
}
