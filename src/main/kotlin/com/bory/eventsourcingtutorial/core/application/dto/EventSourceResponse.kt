package com.bory.eventsourcingtutorial.core.application.dto

import com.bory.eventsourcingtutorial.core.domain.EventSource
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
