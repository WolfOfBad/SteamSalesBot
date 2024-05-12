package ru.wolfofbad.links.exception

class AlreadySubscribedException: SendMessageException() {
    override fun getTelegramMessage(): String {
        return "Вы уже подписаны на эту ссылку. Чтобы посмотреть список своих ссылок, отправьте команду /list"
    }
}
