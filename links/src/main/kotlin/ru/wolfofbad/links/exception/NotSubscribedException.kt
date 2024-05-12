package ru.wolfofbad.links.exception

class NotSubscribedException: SendMessageException() {

    override fun getTelegramMessage(): String {
        return "Вы не подписаны на эту ссылку. Чтобы посмотреть список своих ссылок, отправьте команду /list"
    }
}
