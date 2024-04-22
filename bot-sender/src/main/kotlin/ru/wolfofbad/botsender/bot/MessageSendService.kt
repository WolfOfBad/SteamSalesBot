package ru.wolfofbad.botsender.bot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.BaseResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import ru.wolfofbad.botlistener.retry.policy.BackoffPolicy
import ru.wolfofbad.botsender.bot.exception.MessageNotSentException
import ru.wolfofbad.botsender.dto.request.UpdateRequest

@Service
class MessageSendService(
    private val telegramBot: TelegramBot,
    @Qualifier("telegramBackoff")
    private val backoff: BackoffPolicy
) {
    private val queue: ArrayDeque<UpdateRequest> = ArrayDeque()

    fun sendMessage(request: UpdateRequest) {
        val telegramRequest = SendMessage(request.telegramChatId, request.message)
            .parseMode(ParseMode.HTML)

        var attempts = 1
        var response = processMessage(telegramRequest)
        while (attempts < backoff.getMaxAttempts() && !response.isOk) {
            backoff.getWaitTime(attempts)
            response = processMessage(telegramRequest)
            attempts++
        }

        if (attempts == backoff.getMaxAttempts()) {
            throw MessageNotSentException("Backoff attempts expired and message not sent: $response")
        }
    }

    private fun processMessage(request: SendMessage): BaseResponse {
        val response = telegramBot.execute(request)
        return response
    }

}
