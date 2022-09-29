package com.bory.eventsourcingtutorial.employee.infrastructure.web.query

import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainProjector
import com.bory.eventsourcingtutorial.employee.application.event.*
import com.bory.eventsourcingtutorial.employee.domain.Employee
import com.fasterxml.jackson.databind.ObjectMapper

@DomainProjector
class EmployeeProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Employee> {
    override fun initialLoad(eventSource: EventSource) =
        objectMapper.readValue(eventSource.payload, EmployeeCreatedEvent::class.java).employee

    override fun eventCases(): Map<Class<out Any>, (Employee, EventSource) -> Employee> = mapOf(
        EmployeeUpdatedEvent::class.java to this::updateEmployee,
        EmployeeDeletedEvent::class.java to { employee, _ -> employee.delete() },
        EmployeeAssignedToProjectEvent::class.java to this::assignToProject,
        EmployeeUnassignedFromProjectEvent::class.java to this::unassignFromProject,
        EmployeeMovedToDepartmentEvent::class.java to this::moveToDepartment,
    )

    fun updateEmployee(employee: Employee, eventSource: EventSource) = employee.apply {
        val payloadEmployee =
            objectMapper.readValue(eventSource.payload!!, EmployeeUpdatedEvent::class.java).employee
        updateWith(payloadEmployee)
    }

    fun assignToProject(employee: Employee, eventSource: EventSource) = employee.apply {
        val projectUuid = objectMapper.readValue(
            eventSource.payload!!,
            EmployeeAssignedToProjectEvent::class.java
        ).projectUuid
        assignProject(projectUuid)
    }

    fun unassignFromProject(employee: Employee, eventSource: EventSource) = employee.apply {
        val projectUuid = objectMapper.readValue(
            eventSource.payload!!,
            EmployeeUnassignedFromProjectEvent::class.java
        ).projectUuid

        unassignProject(projectUuid)
    }

    fun moveToDepartment(employee: Employee, eventSource: EventSource) = employee.apply {
        val departmentUuid = objectMapper.readValue(
            eventSource.payload!!,
            EmployeeMovedToDepartmentEvent::class.java
        ).departmentUuid

        moveToDepartment(departmentUuid)
    }
}