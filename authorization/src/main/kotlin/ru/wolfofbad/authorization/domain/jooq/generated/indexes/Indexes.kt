/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.authorization.domain.jooq.generated.indexes


import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal

import ru.wolfofbad.authorization.domain.jooq.generated.tables.Chat



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val TG_CHAT_ID_INDEX: Index = Internal.createIndex(DSL.name("TG_CHAT_ID_INDEX"), Chat.CHAT, arrayOf(Chat.CHAT.TG_CHAT_ID), false)
