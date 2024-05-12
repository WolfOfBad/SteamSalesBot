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

    override fun add(uri: URI): Link {
        return jooq.insertInto(LINK)
            .columns(LINK.URI)
            .values(uri.toString())
            .returning(*LINK.fields())
            .fetchOneInto(Link::class.java)!!
    }

    override fun delete(uri: URI): Link {
        return jooq.deleteFrom(LINK)
            .where(LINK.URI.eq(uri.toString()))
            .returning(*LINK.fields())
            .fetchOneInto(Link::class.java)!!
    }

    override fun subscribe(link: Link, chat: User): Link {
        jooq.insertInto(CHAT_LINK)
            .columns(CHAT_LINK.LINK_ID, CHAT_LINK.CHAT_ID)
            .values(link.id, chat.id)
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

    override fun getUsers(uri: URI): List<User> {
        return jooq.select(*CHAT.fields())
            .from(CHAT)
            .join(CHAT_LINK).on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            .join(LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .where(LINK.URI.eq(uri.toString()))
            .fetchInto(User::class.java)
    }

}
