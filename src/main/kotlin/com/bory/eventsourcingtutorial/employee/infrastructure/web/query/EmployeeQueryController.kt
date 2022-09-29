package com.bory.eventsourcingtutorial.employee.infrastructure.web.query

import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.QueryController
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import com.bory.eventsourcingtutorial.employee.application.dto.EmployeeDto
import com.bory.eventsourcingtutorial.employee.domain.exception.NoSuchEmployeeException
import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@QueryController
@Transactional(readOnly = true)
@RequestMapping("/api/v1/employees")
class EmployeeQueryController(
    private val eventSourceRepository: EventSourceRepository,
    private val employeeProjector: EmployeeProjector,
    private val employeeRepository: EmployeeRepository
) {
    @GetMapping("/{aggregateId}")
    fun get(@PathVariable("aggregateId") aggregateId: String): EmployeeDto {
        val eventSources =
            eventSourceRepository.findByAggregateIdOrderByCreatedAt(aggregateId = aggregateId)
        if (eventSources.isEmpty()) throw NoSuchClientException("EventSource for AggregateId[$aggregateId] not found")

        return employeeProjector.project(eventSources).toDto()
    }

    @GetMapping("/snapshot/{uuid}")
    fun snapshot(@PathVariable("uuid") uuid: String) =
        employeeRepository.findById(uuid)
            .orElseThrow { NoSuchEmployeeException("Employee uuid[$uuid] not found.") }
            .toDto()
}