/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.links.domain.jooq.generated.tables


import java.time.OffsetDateTime
import java.util.function.Function

import javax.annotation.processing.Generated

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row3
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl

import ru.wolfofbad.links.domain.jooq.generated.DefaultSchema
import ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_8
import ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_86
import ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_868
import ru.wolfofbad.links.domain.jooq.generated.tables.records.ChatLinkRecord


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = [
        "https://www.jooq.org",
        "jOOQ version:3.18.13"
    ],
    comments = "This class is generated by jOOQ"
)
@Suppress("UNCHECKED_CAST")
open class ChatLink(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, ChatLinkRecord>?,
    aliased: Table<ChatLinkRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<ChatLinkRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>CHAT_LINK</code>
         */
        val CHAT_LINK: ChatLink = ChatLink()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<ChatLinkRecord> = ChatLinkRecord::class.java

    /**
     * The column <code>CHAT_LINK.CHAT_ID</code>.
     */
    val CHAT_ID: TableField<ChatLinkRecord, Long?> = createField(DSL.name("CHAT_ID"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>CHAT_LINK.LINK_ID</code>.
     */
    val LINK_ID: TableField<ChatLinkRecord, Long?> = createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>CHAT_LINK.LAST_UPDATE</code>.
     */
    val LAST_UPDATE: TableField<ChatLinkRecord, OffsetDateTime?> = createField(DSL.name("LAST_UPDATE"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<ChatLinkRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<ChatLinkRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>CHAT_LINK</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>CHAT_LINK</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>CHAT_LINK</code> table reference
     */
    constructor(): this(DSL.name("CHAT_LINK"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, ChatLinkRecord>): this(Internal.createPathAlias(child, key), child, key, CHAT_LINK, null)
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getUniqueKeys(): List<UniqueKey<ChatLinkRecord>> = listOf(CONSTRAINT_868)
    override fun getReferences(): List<ForeignKey<ChatLinkRecord, *>> = listOf(CONSTRAINT_8, CONSTRAINT_86)

    private lateinit var _chat: Chat
    private lateinit var _link: Link

    /**
     * Get the implicit join path to the <code>PUBLIC.CHAT</code> table.
     */
    fun chat(): Chat {
        if (!this::_chat.isInitialized)
            _chat = Chat(this, CONSTRAINT_8)

        return _chat;
    }

    val chat: Chat
        get(): Chat = chat()

    /**
     * Get the implicit join path to the <code>PUBLIC.LINK</code> table.
     */
    fun link(): Link {
        if (!this::_link.isInitialized)
            _link = Link(this, CONSTRAINT_86)

        return _link;
    }

    val link: Link
        get(): Link = link()
    override fun `as`(alias: String): ChatLink = ChatLink(DSL.name(alias), this)
    override fun `as`(alias: Name): ChatLink = ChatLink(alias, this)
    override fun `as`(alias: Table<*>): ChatLink = ChatLink(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): ChatLink = ChatLink(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): ChatLink = ChatLink(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): ChatLink = ChatLink(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<Long?, Long?, OffsetDateTime?> = super.fieldsRow() as Row3<Long?, Long?, OffsetDateTime?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, Long?, OffsetDateTime?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, Long?, OffsetDateTime?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
