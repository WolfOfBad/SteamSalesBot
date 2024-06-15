package ru.wolfofbad.links.domain

import ru.wolfofbad.links.domain.dto.Link
import ru.wolfofbad.links.domain.dto.User
import java.net.URI
import java.time.Duration
import java.time.OffsetDateTime

interface LinkRepository {
    fun findByUri(uri: URI): Link?

    fun getAllByChatId(chatId: Long): List<Link>

    fun addByUri(uri: URI): Link

    fun delete(link: Link): Link

    fun subscribe(link: Link, chat: User): Link

    fun unsubscribe(link: Link, chat: User): Link

    fun getUsers(link: Link): List<User>

    fun getUsersToUpdate(link: Link, interval: Duration): List<User>
}
