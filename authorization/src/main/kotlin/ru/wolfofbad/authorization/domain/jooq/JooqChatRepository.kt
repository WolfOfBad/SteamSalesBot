package ru.wolfofbad.authorization.domain.jooq

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.wolfofbad.authorization.domain.ChatRepository
import ru.wolfofbad.authorization.domain.jooq.generated.tables.references.CHAT
import ru.wolfofbad.authorization.dto.TgChat
import kotlin.jvm.optionals.getOrNull

@Repository
class JooqChatRepository(
    private val jooq: DSLContext
) : ChatRepository {
    override fun getByTgChatId(tgChatId: Long): TgChat? {
        return jooq.select(*CHAT.fields())
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(TgChat::class.java)
            .getOrNull()
    }

    override fun add(tgChatId: Long): TgChat {
        return jooq.insertInto(CHAT)
            .columns(CHAT.TG_CHAT_ID)
            .values(tgChatId)
            .returning(*CHAT.fields())
            .fetchInto(TgChat::class.java)
            .first()
    }

    override fun removeByTgChatId(tgChatId: Long): TgChat {
        return jooq.deleteFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .returning(*CHAT.fields())
            .fetchInto(TgChat::class.java)
            .first()
    }
}
