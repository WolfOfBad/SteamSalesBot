package ru.wolfofbad.links.domain.jooq

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.wolfofbad.links.domain.ChatRepository
import ru.wolfofbad.links.domain.dto.User
import ru.wolfofbad.links.domain.jooq.generated.tables.references.CHAT

@Repository
class JooqChatRepository(
    private val jooq: DSLContext
) : ChatRepository {
    override fun findById(id: Long): User? {
        return jooq.select(*CHAT.fields())
            .from(CHAT)
            .where(CHAT.ID.eq(id))
            .fetchInto(User::class.java)
            .firstOrNull()
    }

    override fun addUser(id: Long): User {
        return jooq.insertInto(CHAT)
            .columns(CHAT.ID)
            .values(id)
            .returning(*CHAT.fields())
            .fetchOneInto(User::class.java)!!
    }

    override fun deleteUser(id: Long): User {
        return jooq.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .returning(*CHAT.fields())
            .fetchOneInto(User::class.java)!!
    }
}
