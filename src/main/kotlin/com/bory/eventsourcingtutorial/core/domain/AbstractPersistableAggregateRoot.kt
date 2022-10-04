package com.bory.eventsourcingtutorial.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.Instant
import java.util.*

@JsonIgnoreProperties(value = ["id", "new"])
abstract class AbstractPersistableAggregateRoot(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    @Version
    var version: Int = 0,
    @field:CreatedDate
    var createdAt: Instant? = null,
    @field:LastModifiedDate
    var updatedAt: Instant? = null,
) : AbstractAggregateRoot<AbstractPersistableAggregateRoot>() {

    override fun toString(): String {
        return "AbstractEventSourceAggregateRoot(uuid=$uuid, createdAt=$createdAt)"
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