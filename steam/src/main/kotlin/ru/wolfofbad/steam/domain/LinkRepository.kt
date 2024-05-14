package ru.wolfofbad.steam.domain

import ru.wolfofbad.steam.domain.dto.Link
import java.net.URI

interface LinkRepository {
    fun add(uri: URI): Link

    fun delete(uri: URI): Link

    fun getAll(): List<Link>
}
