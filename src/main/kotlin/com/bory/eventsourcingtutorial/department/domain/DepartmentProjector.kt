package com.bory.eventsourcingtutorial.department.domain

import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.department.application.event.DepartmentCreatedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentDeletedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentUpdatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class DepartmentProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Department> {
    override fun initialLoad(eventSource: EventSource): Department =
        objectMapper.readValue(eventSource.payload, DepartmentCreatedEvent::class.java).department

    override fun processEachEventSource(previous: Department, eventSource: EventSource) =
        when (eventSource.type) {
            DepartmentUpdatedEvent::class.java.canonicalName ->
                updateDepartment(previous, eventSource)
            DepartmentDeletedEvent::class.java.canonicalName -> deleteDepartment(previous)
            else -> throw java.lang.IllegalArgumentException("Event Type[${eventSource.type}] Not Supported")
        }

    private fun updateDepartment(department: Department, eventSource: EventSource) =
        department.apply {
            val payloadDepartment = objectMapper.readValue(
                eventSource.payload!!,
                DepartmentUpdatedEvent::class.java
            ).department

            this.updateWith(payloadDepartment)
        }

    private fun deleteDepartment(department: Department) = department.delete()
}