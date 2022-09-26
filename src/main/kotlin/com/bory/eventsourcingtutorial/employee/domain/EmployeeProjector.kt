package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.employee.application.event.EmployeeCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class EmployeeProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Employee> {
    override fun initialLoad(eventSource: EventSource) =
        objectMapper.readValue(eventSource.payload, EmployeeCreatedEvent::class.java).employee

    override fun eventCases(): Map<Class<out Any>, (Employee, EventSource) -> Employee> = mapOf(

    )
}