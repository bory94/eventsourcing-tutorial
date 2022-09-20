package com.bory.eventsourcingtutorial.domain.core

import com.bory.eventsourcingtutorial.infrastructure.repository.core.EventSourceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EventSourceService(
    private val eventSourceRepository: EventSourceRepository
) {
    fun create(eventSource: EventSource) = eventSourceRepository.save(eventSource)
}