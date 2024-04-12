package ru.wolfofbad.botlistener.bot.command.impl

import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import ru.wolfofbad.botlistener.dto.request.UpdateRequest
import ru.wolfofbad.botlistener.kafka.MessagesQueueProducer
import ru.wolfofbad.botlistener.bot.command.Command
import ru.wolfofbad.botlistener.bot.command.parseTgId

class HelpCommand(
    private val messagesQueueProducer: MessagesQueueProducer,
) : Command {
    private val logger = LogManager.getLogger()!!

    override fun getMessage(): String {
        return "Список доступных команд"
    }

    override fun getCommand(): String {
        return "help"
    }

    override fun execute(update: Update) {
        val id = parseTgId(update) ?: return Unit
            .also { logger.error("Update has null id") }

        messagesQueueProducer.sendRequest(
            UpdateRequest(
                id,
                message
            )
        )
    }
}

private const val message = """Список команд бота:
/start начать работу с ботом
/reset сбросить все отслеживаемые ссылки
/track начать отслеживание сслыки
/untrack прекратить отслеживанте ссылки
/list посмотреть список отслеживаемых сайтов
/help список доступных команд
"""
