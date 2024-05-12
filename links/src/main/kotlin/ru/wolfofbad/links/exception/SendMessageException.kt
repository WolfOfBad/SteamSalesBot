package ru.wolfofbad.links.exception

abstract class SendMessageException: RuntimeException() {

    abstract fun getTelegramMessage(): String
}
