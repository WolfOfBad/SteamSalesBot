package ru.wolfofbad.links.exception

class NoLinksException: SendMessageException() {
    override fun getTelegramMessage(): String {
        return "Вы не подписаны ни на одну ссылку. Чтобы начать остеживать скидки, используйте команду /track"
    }
}
