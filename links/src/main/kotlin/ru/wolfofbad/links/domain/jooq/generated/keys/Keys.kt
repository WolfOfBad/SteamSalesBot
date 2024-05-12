/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.links.domain.jooq.generated.keys


import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal

import ru.wolfofbad.links.domain.jooq.generated.tables.Chat
import ru.wolfofbad.links.domain.jooq.generated.tables.ChatLink
import ru.wolfofbad.links.domain.jooq.generated.tables.Link
import ru.wolfofbad.links.domain.jooq.generated.tables.records.ChatLinkRecord
import ru.wolfofbad.links.domain.jooq.generated.tables.records.ChatRecord
import ru.wolfofbad.links.domain.jooq.generated.tables.records.LinkRecord



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val CONSTRAINT_1: UniqueKey<ChatRecord> = Internal.createUniqueKey(Chat.CHAT, DSL.name("CONSTRAINT_1"), arrayOf(Chat.CHAT.ID), true)
val CONSTRAINT_868: UniqueKey<ChatLinkRecord> = Internal.createUniqueKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_868"), arrayOf(ChatLink.CHAT_LINK.CHAT_ID, ChatLink.CHAT_LINK.LINK_ID), true)
val CONSTRAINT_2: UniqueKey<LinkRecord> = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_2"), arrayOf(Link.LINK.ID), true)
val CONSTRAINT_23: UniqueKey<LinkRecord> = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_23"), arrayOf(Link.LINK.URI), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val CONSTRAINT_8: ForeignKey<ChatLinkRecord, ChatRecord> = Internal.createForeignKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_8"), arrayOf(ChatLink.CHAT_LINK.CHAT_ID), ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_1, arrayOf(Chat.CHAT.ID), true)
val CONSTRAINT_86: ForeignKey<ChatLinkRecord, LinkRecord> = Internal.createForeignKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_86"), arrayOf(ChatLink.CHAT_LINK.LINK_ID), ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_2, arrayOf(Link.LINK.ID), true)
