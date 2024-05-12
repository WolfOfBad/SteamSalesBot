package ru.wolfofbad.botlistener.bot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SetMyCommands
import jakarta.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.bot.command.Command
import ru.wolfofbad.botlistener.bot.command.CommandParser
import ru.wolfofbad.botlistener.kafka.producer.MessagesQueueProducer

@Service
class BotService(
    private val bot: TelegramBot,
    private val commandParser: CommandParser,

    private val messagesProducer: MessagesQueueProducer,

    @Qualifier("commandMap")
    commands: LinkedHashMap<String, Command>,
) : UpdatesListener, AutoCloseable {
    private val logger = LogManager.getLogger(BotService::class.java.name)

    init {
        setCommandsMenu(commands)
    }

    @PostConstruct
    fun postConstruct() {
        bot.setUpdatesListener(this)
    }

    private fun setCommandsMenu(commands: LinkedHashMap<String, Command>) {
        val request = SetMyCommands(*commands
            .map { entry -> BotCommand(entry.value.getCommand(), entry.value.getMessage()) }
            .toTypedArray()
        )

        val response = bot.execute(request)

        if (response.isOk) {
            logger.info("Commands menu successfully set")
        } else {
            logger.info("Commands menu not set:" + response.errorCode())
        }
    }

    override fun process(updates: MutableList<Update>?): Int {
        for (update in updates!!) {
            try {
                update.message() ?: continue

                val command = commandParser.parse(update)
                command.execute(update)
            } catch (e: Exception) {
                logger.error(e.message)
                messagesProducer.sendDlqMessage(e)
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    override fun close() {
        bot.shutdown()
    }
}
