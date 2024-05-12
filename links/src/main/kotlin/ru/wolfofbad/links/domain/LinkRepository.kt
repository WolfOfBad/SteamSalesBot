package ru.wolfofbad.links.domain

import ru.wolfofbad.links.domain.dto.Link
import ru.wolfofbad.links.domain.dto.User
import java.net.URI

interface LinkRepository {
    fun findByUri(uri: URI): Link?

    fun getAllByChatId(chatId: Long): List<Link>

    fun add(uri: URI): Link

    fun delete(uri: URI): Link

    fun subscribe(link: Link, chat: User): Link

    fun unsubscribe(link: Link, chat: User): Link

    fun getUsers(uri: URI): List<User>
}
