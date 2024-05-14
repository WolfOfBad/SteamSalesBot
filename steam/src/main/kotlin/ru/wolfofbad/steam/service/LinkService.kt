package ru.wolfofbad.steam.service

import java.net.URI

interface LinkService {
    fun add(uri: URI)

    fun delete(uri: URI)

    fun checkUpdates()
}
