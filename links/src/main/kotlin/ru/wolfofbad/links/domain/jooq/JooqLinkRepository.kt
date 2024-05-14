package ru.wolfofbad.links.domain.jooq

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.wolfofbad.links.domain.LinkRepository
import ru.wolfofbad.links.domain.dto.Link
import ru.wolfofbad.links.domain.dto.User
import ru.wolfofbad.links.domain.jooq.generated.tables.references.CHAT
import ru.wolfofbad.links.domain.jooq.generated.tables.references.CHAT_LINK
import ru.wolfofbad.links.domain.jooq.generated.tables.references.LINK
import java.net.URI
import java.time.OffsetDateTime

@Repository
class JooqLinkRepository(
    private val jooq: DSLContext
) : LinkRepository {
    override fun findByUri(uri: URI): Link? {
        return jooq.select(*LINK.fields())
            .from(LINK)
            .where(LINK.URI.eq(uri.toString()))
            .fetchOneInto(Link::class.java)
    }

    override fun getAllByChatId(chatId: Long): List<Link> {
        return jooq.select(*LINK.fields())
            .from(LINK)
            .join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .join(CHAT).on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            .fetchInto(Link::class.java)
    }

    override fun addByUri(uri: URI): Link {
        return jooq.insertInto(LINK)
            .columns(LINK.URI)
            .values(uri.toString())
            .returning(*LINK.fields())
            .fetchOneInto(Link::class.java)!!
    }

    override fun delete(link: Link): Link {
        return jooq.deleteFrom(LINK)
            .where(LINK.ID.eq(link.id))
            .returning(*LINK.fields())
            .fetchOneInto(Link::class.java)!!
    }

    override fun subscribe(link: Link, chat: User): Link {
        jooq.insertInto(CHAT_LINK)
            .columns(CHAT_LINK.LINK_ID, CHAT_LINK.CHAT_ID, CHAT_LINK.LAST_UPDATE)
            .values(link.id, chat.id, OffsetDateTime.now())
            .execute()

        return link
    }

    override fun unsubscribe(link: Link, chat: User): Link {
        jooq.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(link.id))
            .and(CHAT_LINK.CHAT_ID.eq(chat.id))
            .execute()

        return link
    }

    override fun getUsers(link: Link): List<User> {
        return jooq.select(*CHAT.fields())
            .from(CHAT)
            .join(CHAT_LINK).on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            .join(LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .where(LINK.ID.eq(link.id))
            .fetchInto(User::class.java)
    }

    override fun getLastUpdate(link: Link, chat: User): OffsetDateTime {
        return jooq.select(CHAT_LINK.LAST_UPDATE)
            .from(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(link.id))
            .and(CHAT_LINK.CHAT_ID.eq(chat.id))
            .fetchOneInto(OffsetDateTime::class.java)!!
    }

}
