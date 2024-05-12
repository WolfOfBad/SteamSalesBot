package ru.wolfofbad.botlistener.bot.command.impl

import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import ru.wolfofbad.botlistener.kafka.producer.AuthorizationQueueProducer
import ru.wolfofbad.botlistener.dto.request.AuthorizeRequest
import ru.wolfofbad.botlistener.bot.command.Command
import ru.wolfofbad.botlistener.bot.command.parseTgId

class ResetCommand(
    private val authorizationQueueProducer: AuthorizationQueueProducer
) : Command {
    private val logger = LogManager.getLogger()

    override fun getMessage(): String {
        return "Сбросить все отслеживаемые ссылки"
    }

    override fun getCommand(): String {
        return "reset"
    }

    override fun execute(update: Update) {
        val id = parseTgId(update) ?: return Unit
            .also { logger.error("Update has null id") }

        authorizationQueueProducer.sendAuthorizationRequest(AuthorizeRequest(id, AuthorizeRequest.Type.RESET))
    }
}
