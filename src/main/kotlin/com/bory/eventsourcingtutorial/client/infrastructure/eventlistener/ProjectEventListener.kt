package com.bory.eventsourcingtutorial.client.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.client.application.event.ProjectDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectUpdatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectsAddedEvent
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class ProjectEventListener(
    private val eventSourceRepository: EventSourceRepository
) {
    @Async
    @EventListener(classes = [ProjectsAddedEvent::class])
    fun on(event: ProjectsAddedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.clientUuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [ProjectUpdatedEvent::class])
    fun on(event: ProjectUpdatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.clientUuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [ProjectDeletedEvent::class])
    fun on(event: ProjectDeletedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.clientUuid, event = event)
        )
    }
}