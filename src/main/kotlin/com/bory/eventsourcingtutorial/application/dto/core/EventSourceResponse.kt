package com.bory.eventsourcingtutorial.application.dto.core

import com.bory.eventsourcingtutorial.domain.core.EventSource
import org.springframework.http.ResponseEntity
import java.time.Instant

data class EventSourceResponse(
    val uuid: String,
    val type: String?,
    val version: Int?,
    val aggregateId: String?,
    val createdAt: Instant?
) {
    constructor(eventSource: EventSource) : this(
        eventSource.uuid,
        eventSource.type,
        eventSource.version,
        eventSource.aggregateId,
        eventSource.createdAt
    )

    fun acceptedResponse() = ResponseEntity.accepted().body(this)
}
