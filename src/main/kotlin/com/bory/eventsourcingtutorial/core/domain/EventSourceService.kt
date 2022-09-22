package com.bory.eventsourcingtutorial.core.domain

import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EventSourceService(
    private val eventSourceRepository: EventSourceRepository
) {
    fun create(eventSource: EventSource) = eventSourceRepository.save(eventSource)
}