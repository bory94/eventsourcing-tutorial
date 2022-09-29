package com.bory.eventsourcingtutorial.department.infrastructure.web.query

import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainProjector
import com.bory.eventsourcingtutorial.department.application.event.DepartmentCreatedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentDeletedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentUpdatedEvent
import com.bory.eventsourcingtutorial.department.domain.Department
import com.fasterxml.jackson.databind.ObjectMapper

@DomainProjector
class DepartmentProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Department> {
    override fun initialLoad(eventSource: EventSource): Department =
        objectMapper.readValue(eventSource.payload, DepartmentCreatedEvent::class.java).department

    override fun eventCases(): Map<Class<out Any>, (Department, EventSource) -> Department> = mapOf(
        DepartmentUpdatedEvent::class.java to this::updateDepartment,
        DepartmentDeletedEvent::class.java to { department, _ -> department.delete() }
    )

    private fun updateDepartment(department: Department, eventSource: EventSource) =
        department.apply {
            val payloadDepartment = objectMapper.readValue(
                eventSource.payload!!,
                DepartmentUpdatedEvent::class.java
            ).department

            this.updateWith(payloadDepartment)
        }
}