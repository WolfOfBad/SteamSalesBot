/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.links.domain.jooq.generated.tables


import java.util.function.Function

import javax.annotation.processing.Generated

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Index
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row2
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
import ru.wolfofbad.links.domain.jooq.generated.indexes.URI_INDEX
import ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_2
import ru.wolfofbad.links.domain.jooq.generated.keys.CONSTRAINT_23
import ru.wolfofbad.links.domain.jooq.generated.tables.records.LinkRecord


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
open class Link(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, LinkRecord>?,
    aliased: Table<LinkRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<LinkRecord>(
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
         * The reference instance of <code>LINK</code>
         */
        val LINK: Link = Link()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<LinkRecord> = LinkRecord::class.java

    /**
     * The column <code>LINK.ID</code>.
     */
    val ID: TableField<LinkRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>LINK.URI</code>.
     */
    val URI: TableField<LinkRecord, String?> = createField(DSL.name("URI"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<LinkRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<LinkRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>LINK</code> table reference
     */
    constructor(): this(DSL.name("LINK"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, LinkRecord>): this(Internal.createPathAlias(child, key), child, key, LINK, null)
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getIndexes(): List<Index> = listOf(URI_INDEX)
    override fun getIdentity(): Identity<LinkRecord, Long?> = super.getIdentity() as Identity<LinkRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<LinkRecord> = CONSTRAINT_2
    override fun getUniqueKeys(): List<UniqueKey<LinkRecord>> = listOf(CONSTRAINT_23)
    override fun `as`(alias: String): Link = Link(DSL.name(alias), this)
    override fun `as`(alias: Name): Link = Link(alias, this)
    override fun `as`(alias: Table<*>): Link = Link(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Link = Link(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Link = Link(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Link = Link(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}