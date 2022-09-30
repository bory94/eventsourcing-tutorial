package com.bory.eventsourcingtutorial.employee.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import com.bory.eventsourcingtutorial.employee.application.event.*
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class EmployeeEventListener(
    private val eventSourceRepository: EventSourceRepository
) {
    @Async
    @EventListener(classes = [EmployeeCreatedEvent::class])
    fun on(event: EmployeeCreatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employee.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [EmployeeUpdatedEvent::class])
    fun on(event: EmployeeUpdatedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employee.uuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [EmployeeDeletedEvent::class])
    fun on(event: EmployeeDeletedEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employeeUuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [EmployeeAssignRequestedToProjectEvent::class])
    fun on(event: EmployeeAssignRequestedToProjectEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employeeUuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [EmployeeUnassignRequestedFromProjectEvent::class])
    fun on(event: EmployeeUnassignRequestedFromProjectEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employeeUuid, event = event)
        )
    }

    @Async
    @EventListener(classes = [EmployeeMoveRequestedToDepartmentEvent::class])
    fun on(event: EmployeeMoveRequestedToDepartmentEvent) {
        eventSourceRepository.save(
            EventSource(aggregateId = event.employeeUuid, event = event)
        )
    }
}