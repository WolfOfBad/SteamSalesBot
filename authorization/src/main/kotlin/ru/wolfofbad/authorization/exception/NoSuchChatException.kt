package ru.wolfofbad.authorization.exception

class NoSuchChatException : SendMessageException() {
    override fun getTelegramMessage(): String {
        return "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом"
    }
}
