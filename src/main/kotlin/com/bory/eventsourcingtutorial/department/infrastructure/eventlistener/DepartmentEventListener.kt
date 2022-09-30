package com.bory.eventsourcingtutorial.department.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import com.bory.eventsourcingtutorial.department.application.event.DepartmentCreatedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentDeletedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentUpdatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class DepartmentEventListener(
    private val eventSourceRepository: EventSourceRepository
) {
    @Async
    @EventListener(classes = [DepartmentCreatedEvent::class])
    fun on(event: DepartmentCreatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.department.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [DepartmentUpdatedEvent::class])
    fun on(event: DepartmentUpdatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.department.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [DepartmentDeletedEvent::class])
    fun on(event: DepartmentDeletedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.departmentUuid, event = event)
        )
    }
}