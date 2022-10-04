package com.bory.eventsourcingtutorial.department.domain

import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
import com.bory.eventsourcingtutorial.department.application.command.CreateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.UpdateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.dto.DepartmentDto
import com.bory.eventsourcingtutorial.department.application.event.*
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("department")
class Department(
    uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var deleted: Boolean = false,
    version: Int = 0,
    createdAt: Instant? = null,
    updatedAt: Instant? = null,

    @MappedCollection(idColumn = "department_uuid", keyColumn = "uuid")
    var departmentTeamMembers: List<DepartmentTeamMember> = mutableListOf(),
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, updatedAt) {
    companion object {
        private const val MAXIMUM_TEAM_SIZE = 8
    }

    @PersistenceCreator
    constructor(
        uuid: String,
        name: String, description: String, deleted: Boolean,
        version: Int,
        createdAt: Instant,
        updatedAt: Instant,
    ) : this(
        uuid,
        name, description, deleted,
        version,
        createdAt, updatedAt,
        mutableListOf()
    )

    constructor(uuid: String, command: CreateDepartmentCommand) : this(
        uuid = uuid,
        name = command.department.name,
        description = command.department.name,
        deleted = command.department.deleted
    ) {
        registerEvent(DepartmentCreatedEvent(this))
    }

    constructor(uuid: String, command: UpdateDepartmentCommand) : this(
        uuid = uuid,
        name = command.departmentDto.name,
        description = command.departmentDto.name,
        deleted = command.departmentDto.deleted
    )

    fun updateWith(updating: Department) = this.apply {
        name = updating.name
        description = updating.description
        deleted = updating.deleted
        updatedAt = Instant.now()

        registerEvent(DepartmentUpdatedEvent(this))
    }

    fun delete() = this.apply {
        deleted = true

        registerEvent(DepartmentDeletedEvent(this.uuid))
    }

    fun toDto() = DepartmentDto(
        uuid = uuid,
        name = name,
        description = description,
        deleted = deleted,
        version = version,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
        departmentTeamMembers = departmentTeamMembers.map(DepartmentTeamMember::toDto)
    )

    fun tryAcceptTeamMember(teamMember: DepartmentTeamMember) = this.apply {
        if (departmentTeamMembers.size >= MAXIMUM_TEAM_SIZE) {
            throw IllegalStateException("Team size is full. Cannot accept team members more.")
        }

        departmentTeamMembers += teamMember

        registerEvent(EmployeeMoveAcceptedEvent(teamMember.employeeUuid, this.uuid))
    }

    fun tryReleaseTeamMember(employeeUuid: String) = this.apply {
        if (departmentTeamMembers.size <= 1) {
            throw IllegalStateException("Team size is too small. Cannot release team members more.")
        }
        departmentTeamMembers = departmentTeamMembers.filter { it.employeeUuid != employeeUuid }

        registerEvent(EmployeeReleasedEvent(employeeUuid, this.uuid))
    }
}