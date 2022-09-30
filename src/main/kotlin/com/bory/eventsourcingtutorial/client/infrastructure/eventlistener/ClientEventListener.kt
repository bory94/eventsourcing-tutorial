package com.bory.eventsourcingtutorial.client.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class ClientEventListener(
    private val eventSourceRepository: EventSourceRepository
) {
    @Async
    @EventListener(classes = [ClientCreatedEvent::class])
    fun on(event: ClientCreatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.client.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [ClientUpdatedEvent::class])
    fun on(event: ClientUpdatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.client.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [ClientDeletedEvent::class])
    fun on(event: ClientDeletedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.clientUuid, event = event)
        )
    }
}