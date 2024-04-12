package ru.wolfofbad.botlistener.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.wolfofbad.botlistener.kafka.AuthorizationQueueProducer
import ru.wolfofbad.botlistener.kafka.MessagesQueueProducer
import ru.wolfofbad.botlistener.service.command.Command
import ru.wolfofbad.botlistener.service.command.impl.*
import ru.wolfofbad.botlistener.service.linkcheck.LinkChecker
import ru.wolfofbad.botlistener.service.linkcheck.SteamLinkParser

@Configuration
class ApplicationConfiguration {
    @Bean("commandMap")
    fun commandMap(
        authorizationQueueProducer: AuthorizationQueueProducer,
        messagesQueueProducer: MessagesQueueProducer,
        @Qualifier("linkChainParser")
        linkCheckerChain: LinkChecker
    ): LinkedHashMap<String, Command> {
        val map = LinkedHashMap<String, Command>()

        val start = StartCommand(authorizationQueueProducer)
        val help = HelpCommand(messagesQueueProducer)
        val track = TrackCommand(authorizationQueueProducer, messagesQueueProducer, linkCheckerChain)
        val untrack = UntrackCommand(authorizationQueueProducer, messagesQueueProducer, linkCheckerChain)
        val list = ListCommand(authorizationQueueProducer)
        val reset = ResetCommand(authorizationQueueProducer)

        map.putLast(start.getCommand(), start)
        map.putLast(help.getCommand(), help)
        map.putLast(track.getCommand(), track)
        map.putLast(untrack.getCommand(), untrack)
        map.putLast(list.getCommand(), list)
        map.putLast(reset.getCommand(), reset)

        return map
    }

    @Bean("linkChainParser")
    fun linkChainParser(): LinkChecker {
        return LinkChecker.link(
            SteamLinkParser()
        )
    }
}
