package ru.wolfofbad.botlistener.bot.command.impl

import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import ru.wolfofbad.botlistener.dto.request.LinkRequest
import ru.wolfofbad.botlistener.dto.request.UpdateRequest
import ru.wolfofbad.botlistener.kafka.AuthorizationQueueProducer
import ru.wolfofbad.botlistener.kafka.MessagesQueueProducer
import ru.wolfofbad.botlistener.bot.command.Command
import ru.wolfofbad.botlistener.bot.command.getLinkFromUpdate
import ru.wolfofbad.botlistener.bot.command.parseTgId
import ru.wolfofbad.botlistener.bot.linkcheck.LinkChecker

class TrackCommand(
    private val authorizationQueueProducer: AuthorizationQueueProducer,
    private val messagesQueueProducer: MessagesQueueProducer,
    private val linkChecker: LinkChecker
) : Command {
    private val logger = LogManager.getLogger()

    override fun getMessage(): String {
        return "Начать отслеживание ссылки"
    }

    override fun getCommand(): String {
        return "track"
    }

    override fun execute(update: Update) {
        val id = parseTgId(update) ?: return Unit
            .also { logger.error("Update has null id") }

        val text = getLinkFromUpdate(update) ?: return Unit
            .also {
                messagesQueueProducer.sendRequest(
                    UpdateRequest(id, "Чтобы начать отслеживать ресурс, отправьте /track и ссылку на этот ресурс")
                )
            }

        val link = linkChecker.parse(text) ?: return Unit
            .also {
                messagesQueueProducer.sendRequest(
                    UpdateRequest(
                        id,
                        "Данный ресурс, к сожалению, пока не поддерживается"
                    )
                )
            }

        authorizationQueueProducer.sendLinkRequest(LinkRequest(id, link, LinkRequest.Type.TRACK))
    }
}
