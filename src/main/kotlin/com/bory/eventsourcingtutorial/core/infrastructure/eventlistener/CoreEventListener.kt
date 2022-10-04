package com.bory.eventsourcingtutorial.core.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@DomainEventListener
class CoreEventListener(
    private val eventSourceRepository: EventSourceRepository
) {
    @TransactionalEventListener(
        phase = TransactionPhase.BEFORE_COMMIT
    )
    fun on(event: AbstractCustomEvent) {
        eventSourceRepository.save(EventSource(aggregateId = event.aggregateUuid, event = event))
    }
}