package ru.wolfofbad.botlistener.service.command

import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update

interface Command {
    fun getMessage() : String?

    fun getCommand() : String?

    fun execute(update: Update)
}

fun parseTgId(update: Update) : Long? {
    return update.message()?.from()?.id()
}

fun getLinkFromUpdate(update: Update) : String? {
    val entity = update.message().entities()?.first { e -> e.type() == MessageEntity.Type.bot_command } ?: return null

    val length = entity.length()
    if (update.message().text().length <= length) {
        return null
    }

    return update.message().text().substring(length + 1)
}
