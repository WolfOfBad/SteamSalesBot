package ru.wolfofbad.links.domain

import ru.wolfofbad.links.domain.dto.User

interface ChatRepository {
    fun findById(id: Long): User?

    fun addUser(id: Long): User

    fun deleteUser(id: Long): User
}
