package ru.wolfofbad.links.service

import java.net.URI

interface LinkService {
    fun track(id: Long, uri: URI)

    fun untrack(id: Long, uri: URI)

    fun listLink(id: Long)

    fun deleteUser(id: Long)
}
