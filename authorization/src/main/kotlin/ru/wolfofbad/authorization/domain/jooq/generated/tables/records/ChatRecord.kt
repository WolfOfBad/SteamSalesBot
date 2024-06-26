/*
 * This file is generated by jOOQ.
 */
package ru.wolfofbad.authorization.domain.jooq.generated.tables.records


import jakarta.validation.constraints.NotNull
import org.jetbrains.annotations.Nullable
import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record2
import org.jooq.Row2
import org.jooq.impl.UpdatableRecordImpl
import ru.wolfofbad.authorization.domain.jooq.generated.tables.Chat
import javax.annotation.processing.Generated


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
open class ChatRecord() : UpdatableRecordImpl<ChatRecord>(Chat.CHAT), Record2<Long?, Long?> {

    open var id: Long?
        set(value): Unit = set(0, value)
        @Nullable
        get(): Long? = get(0) as Long?

    @get:NotNull
    open var tgChatId: Long?
        set(value): Unit = set(1, value)
        @org.jetbrains.annotations.NotNull
        get(): Long? = get(1) as Long?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row2<Long?, Long?> = super.fieldsRow() as Row2<Long?, Long?>
    override fun valuesRow(): Row2<Long?, Long?> = super.valuesRow() as Row2<Long?, Long?>
    override fun field1(): Field<Long?> = Chat.CHAT.ID
    override fun field2(): Field<Long?> = Chat.CHAT.TG_CHAT_ID
    override fun component1(): Long? = id
    override fun component2(): Long? = tgChatId
    override fun value1(): Long? = id
    override fun value2(): Long? = tgChatId

    override fun value1(value: Long?): ChatRecord {
        set(0, value)
        return this
    }

    override fun value2(value: Long?): ChatRecord {
        set(1, value)
        return this
    }

    override fun values(value1: Long?, value2: Long?): ChatRecord {
        this.value1(value1)
        this.value2(value2)
        return this
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    constructor(id: Long? = null, tgChatId: Long? = null) : this() {
        this.id = id
        this.tgChatId = tgChatId
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    constructor(value: ru.wolfofbad.authorization.domain.jooq.generated.tables.pojos.Chat?) : this() {
        if (value != null) {
            this.id = value.id
            this.tgChatId = value.tgChatId
            resetChangedOnNotNull()
        }
    }
}
