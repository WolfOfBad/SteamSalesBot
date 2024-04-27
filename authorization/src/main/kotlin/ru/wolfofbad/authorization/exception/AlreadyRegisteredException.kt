package ru.wolfofbad.authorization.exception

class AlreadyRegisteredException : SendMessageException() {
    override fun getTelegramMessage(): String {
        return "Вы уже зарегестрированы в боте. Чтобы сбросить ссылки отправьте команду /reset"
    }
}
