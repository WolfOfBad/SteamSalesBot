/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.links.domain.jooq.generated.tables.records


import jakarta.validation.constraints.NotNull

import javax.annotation.processing.Generated

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Row1
import org.jooq.impl.UpdatableRecordImpl

import ru.wolfofbad.links.domain.jooq.generated.tables.Chat


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
open class ChatRecord() : UpdatableRecordImpl<ChatRecord>(Chat.CHAT), Record1<Long?> {

    @get:NotNull
    open var id: Long?
        set(value): Unit = set(0, value)
    @org.jetbrains.annotations.NotNull
        get(): Long? = get(0) as Long?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row1<Long?> = super.fieldsRow() as Row1<Long?>
    override fun valuesRow(): Row1<Long?> = super.valuesRow() as Row1<Long?>
    override fun field1(): Field<Long?> = Chat.CHAT.ID
    override fun component1(): Long? = id
    override fun value1(): Long? = id

    override fun value1(value: Long?): ChatRecord {
        set(0, value)
        return this
    }

    override fun values(value1: Long?): ChatRecord {
        this.value1(value1)
        return this
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    constructor(id: Long? = null): this() {
        this.id = id
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    constructor(value: ru.wolfofbad.links.domain.jooq.generated.tables.pojos.Chat?): this() {
        if (value != null) {
            this.id = value.id
            resetChangedOnNotNull()
        }
    }
}
