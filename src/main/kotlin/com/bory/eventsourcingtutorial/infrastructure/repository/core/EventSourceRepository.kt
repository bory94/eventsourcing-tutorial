package com.bory.eventsourcingtutorial.infrastructure.repository.core

import com.bory.eventsourcingtutorial.domain.core.EventSource
import org.springframework.data.repository.CrudRepository

interface EventSourceRepository : CrudRepository<EventSource, String> {
    fun findByAggregateIdOrderByCreatedAt(aggregateId: String): List<EventSource>
}