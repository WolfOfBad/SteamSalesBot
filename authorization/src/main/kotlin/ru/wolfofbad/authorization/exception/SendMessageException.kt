package ru.wolfofbad.authorization.exception

abstract class SendMessageException: RuntimeException() {
    abstract fun getTelegramMessage(): String
}
