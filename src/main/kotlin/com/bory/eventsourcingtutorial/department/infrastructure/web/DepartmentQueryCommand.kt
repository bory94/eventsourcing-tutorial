package com.bory.eventsourcingtutorial.department.infrastructure.web

import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.core.infrastructure.persistence.EventSourceRepository
import com.bory.eventsourcingtutorial.department.application.dto.DepartmentDto
import com.bory.eventsourcingtutorial.department.domain.DepartmentProjector
import com.bory.eventsourcingtutorial.department.domain.exception.NoSuchDepartmentException
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/v1/departments")
class DepartmentQueryCommand(
    private val eventSourceRepository: EventSourceRepository,
    private val departmentProjector: DepartmentProjector,
    private val departmentRepository: DepartmentRepository
) {
    @GetMapping("/{aggregateId}")
    fun projectClient(@PathVariable("aggregateId") aggregateId: String): DepartmentDto {
        val eventSources = eventSourceRepository.findByAggregateIdOrderByCreatedAt(aggregateId)
        if (eventSources.isEmpty()) throw NoSuchClientException("EventSource for AggregateId[$aggregateId] not found")

        return departmentProjector.project(eventSources).toDto()
    }

    @GetMapping("/snapshot/{uuid}")
    fun getBySnapshot(@PathVariable("uuid") uuid: String): DepartmentDto =
        departmentRepository.findById(uuid)
            .orElseThrow { throw NoSuchDepartmentException("Department uuid[$uuid] not found.") }
            .toDto()

}