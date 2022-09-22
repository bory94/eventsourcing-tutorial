package com.bory.eventsourcingtutorial.core.infrastructure.persistence

import com.bory.eventsourcingtutorial.core.domain.EventSource
import org.springframework.data.repository.CrudRepository

interface EventSourceRepository : CrudRepository<EventSource, String> {
    fun findByAggregateIdOrderByCreatedAt(aggregateId: String): List<EventSource>
}