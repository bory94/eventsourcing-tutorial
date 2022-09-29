package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.dto.ProjectDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("project")
data class Project(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
    val clientUuid: String,

    @MappedCollection(idColumn = "uuid", keyColumn = "project_uuid")
    var projectAssignees: List<ProjectAssignee> = mutableListOf()
) {
    constructor(clientUuid: String, projectDto: ProjectDto) : this(
        uuid = projectDto.uuid ?: UUID.randomUUID().toString(),
        name = projectDto.name,
        description = projectDto.description,
        clientUuid = clientUuid
    )

    fun updateWith(newProject: Project) {
        name = newProject.name
        description = newProject.description
        updatedAt = Instant.now()
    }

    fun toDto() = ProjectDto(
        uuid = uuid,
        name = name,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        clientUuid = clientUuid,
        projectAssignees = projectAssignees.map(ProjectAssignee::toDto)
    )

    fun assignTeamMember(employeeUuid: String) {
        projectAssignees += ProjectAssignee(projectUuid = uuid, employeeUuid = employeeUuid)
    }

    fun unassignTeamMember(employeeUuid: String) {
        projectAssignees = projectAssignees.filter { it.employeeUuid != employeeUuid }
    }
}