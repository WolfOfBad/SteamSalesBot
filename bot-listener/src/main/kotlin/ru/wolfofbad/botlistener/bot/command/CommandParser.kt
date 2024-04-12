package ru.wolfofbad.botlistener.bot.command

import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.bot.command.impl.NotCommandFail
import ru.wolfofbad.botlistener.bot.command.impl.UnknownCommandFail

@Service
class CommandParser(
    @Qualifier("commandMap")
    private val regularCommands: Map<String, Command>,
    private val notCommandFail: NotCommandFail,
    private val unknownCommandFail: UnknownCommandFail
) {
    fun parse(update: Update): Command {
        val entity = update.message().entities()?.first { e -> e.type() == MessageEntity.Type.bot_command } ?: return notCommandFail

        val length = entity.length()
        val command = update.message().text().substring(1, length)

        val result = regularCommands.getOrDefault(command, unknownCommandFail)
        return result
    }
}
