package ru.wolfofbad.botlistener.service

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SetMyCommands
import jakarta.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.service.command.Command
import ru.wolfofbad.botlistener.service.command.CommandParser

@Service
class BotService(
    private val bot: TelegramBot,
    private val commandParser: CommandParser,
    @Qualifier("commandMap")
    commands: LinkedHashMap<String, Command>
) : UpdatesListener, AutoCloseable {

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

        val logger = LogManager.getLogger()
        if (response.isOk) {
            logger.info("Commands menu successfully set")
        } else {
            logger.info("Commands menu not set:" + response.errorCode())
        }
    }

    override fun process(updates: MutableList<Update>?): Int {
        for (update in updates!!) {
            update.message() ?: continue

            val command = commandParser.parse(update)
            command.execute(update)
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    override fun close() {
        bot.shutdown()
    }
}
