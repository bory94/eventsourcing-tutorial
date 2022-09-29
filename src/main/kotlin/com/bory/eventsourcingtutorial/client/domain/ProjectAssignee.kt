package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.dto.ProjectAssigneeDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("project_assignee")
data class ProjectAssignee(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var projectUuid: String,
    var employeeUuid: String,
    var createdAt: Instant = Instant.now(),
) {
    constructor(projectUuid: String, dto: ProjectAssigneeDto) : this(
        uuid = dto.uuid ?: UUID.randomUUID().toString(),
        projectUuid = projectUuid,
        employeeUuid = dto.employeeUuid,
        createdAt = dto.createdAt
    )

    fun toDto() = ProjectAssigneeDto(
        uuid = uuid,
        projectUuid = projectUuid,
        employeeUuid = employeeUuid,
        createdAt = createdAt
    )
}