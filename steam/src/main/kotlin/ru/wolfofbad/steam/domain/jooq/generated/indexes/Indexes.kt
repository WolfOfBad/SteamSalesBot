/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.steam.domain.jooq.generated.indexes


import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal

import ru.wolfofbad.steam.domain.jooq.generated.tables.Link



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val INDEX_2: Index = Internal.createIndex(DSL.name("INDEX_2"), Link.LINK, arrayOf(Link.LINK.URI), false)