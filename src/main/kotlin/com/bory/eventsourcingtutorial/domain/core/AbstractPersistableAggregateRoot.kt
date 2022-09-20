package com.bory.eventsourcingtutorial.domain.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.Persistable
import java.time.Instant
import java.util.*

@JsonIgnoreProperties(value = ["id", "new", "persisted"])
abstract class AbstractPersistableAggregateRoot(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    @Version
    var version: Int = 1,
    @field:CreatedDate
    var createdAt: Instant? = null,
    @field:org.springframework.data.annotation.Transient
    var persisted: Boolean = true
) : AbstractAggregateRoot<AbstractPersistableAggregateRoot>(), Persistable<String> {

    override fun getId() = uuid
    override fun isNew() = !persisted

    @AfterDomainEventPublication
    fun setPersisted() {
        persisted = true
    }

    override fun toString(): String {
        return "AbstractEventSourceAggregateRoot(uuid=$uuid, createdAt=$createdAt, persisted=$persisted)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractPersistableAggregateRoot) return false

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}