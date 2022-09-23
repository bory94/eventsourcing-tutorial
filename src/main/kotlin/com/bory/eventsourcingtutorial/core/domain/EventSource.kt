package com.bory.eventsourcingtutorial.core.domain

import com.bory.eventsourcingtutorial.core.infrastructure.config.JacksonConfig.Companion.customObjectMapper
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("event_source")
class EventSource(
    uuid: String = UUID.randomUUID().toString(),
    var type: String? = null,
    var aggregateId: String? = null,
    version: Int = 1,
    var payload: String? = null,
    createdAt: Instant? = null,
    persisted: Boolean = false,
    @field:org.springframework.data.annotation.Transient
    val event: Any? = null
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, persisted) {
    @PersistenceCreator
    constructor(
        uuid: String,
        type: String?,
        aggregateId: String?,
        version: Int,
        payload: String?,
        createdAt: Instant
    ) : this(uuid, type, aggregateId, version, payload, createdAt, true, null)

    init {
        if (event != null) {
            type = event.javaClass.canonicalName
            payload = customObjectMapper.writeValueAsString(event)

            if (!persisted) registerEvent(event)
        }
    }

    override fun toString(): String {
        return super.toString() + "\n" + "EventSource(" +
                "type='$type', aggregateId=$aggregateId, payload=$payload" +
                ")"
    }
}
