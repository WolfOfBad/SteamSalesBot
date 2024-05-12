package ru.wolfofbad.botlistener.bot.command.impl

import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.dto.request.UpdateRequest
import ru.wolfofbad.botlistener.kafka.producer.MessagesQueueProducer
import ru.wolfofbad.botlistener.bot.command.Command
import ru.wolfofbad.botlistener.bot.command.parseTgId

@Service
class NotCommandFail(
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
                "Введите команду бота. Вы можете посмотреть список команд, отправив /help"
            )
        )
    }
}
