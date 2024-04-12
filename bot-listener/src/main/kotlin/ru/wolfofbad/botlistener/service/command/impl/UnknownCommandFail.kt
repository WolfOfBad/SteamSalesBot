package ru.wolfofbad.botlistener.service.command.impl

import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.dto.request.UpdateRequest
import ru.wolfofbad.botlistener.kafka.MessagesQueueProducer
import ru.wolfofbad.botlistener.service.command.Command
import ru.wolfofbad.botlistener.service.command.parseTgId

@Service
class UnknownCommandFail(
    private val messagesQueueProducer: MessagesQueueProducer
) : Command {
    private val logger = LogManager.getLogger()

    override fun getMessage(): String? {
        return null
    }

    override fun getCommand(): String? {
        return null
    }

    override fun execute(update: Update) {
        val id = parseTgId(update) ?: return Unit
            .also { logger.error("Update has null id") }

        messagesQueueProducer.sendRequest(
            UpdateRequest(
                id,
                "Неизвестная команда. Чтобы посмотреть список команд, используйте /help"
            )
        )
    }
}
