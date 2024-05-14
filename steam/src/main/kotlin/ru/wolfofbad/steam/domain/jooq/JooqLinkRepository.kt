package ru.wolfofbad.steam.domain.jooq

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.wolfofbad.steam.domain.LinkRepository
import ru.wolfofbad.steam.domain.dto.Link
import ru.wolfofbad.steam.domain.jooq.generated.tables.references.LINK
import java.net.URI

@Repository
class JooqLinkRepository(
    private val jooq: DSLContext
) : LinkRepository {
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

    override fun getAll(): List<Link> {
        return jooq.selectFrom(LINK)
            .fetchInto(Link::class.java)
    }


}
