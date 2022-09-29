package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.client.application.event.ProjectTeamMemberAssignedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectTeamMemberUnassignedEvent
import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
import com.bory.eventsourcingtutorial.employee.application.command.CreateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.command.UpdateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.dto.EmployeeDto
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("employee")
class Employee(
    uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var age: Int,
    var salary: Int,
    var position: String,
    var departmentUuid: String,
    var deleted: Boolean = false,
    version: Int = 1,
    createdAt: Instant? = null,
    updatedAt: Instant? = null,

    @MappedCollection(idColumn = "uuid", keyColumn = "employee_uuid")
    var employeeProjects: List<EmployeeProject> = mutableListOf(),

    persisted: Boolean = false
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, updatedAt, persisted) {
    @PersistenceCreator
    constructor(
        uuid: String,

        name: String, age: Int, salary: Int, position: String,
        departmentUuid: String, deleted: Boolean,

        version: Int,
        createdAt: Instant,
        updatedAt: Instant,
    ) : this(
        uuid,

        name, age, salary, position, departmentUuid, deleted,

        version,
        createdAt, updatedAt,

        mutableListOf(),
        true
    )

    constructor(uuid: String, command: CreateEmployeeCommand) : this(
        uuid,
        command.employeeDto.name,
        command.employeeDto.age,
        command.employeeDto.salary,
        command.employeeDto.position,
        command.employeeDto.departmentUuid,
    )

    constructor(uuid: String, command: UpdateEmployeeCommand) : this(
        uuid,
        command.employeeDto.name,
        command.employeeDto.age,
        command.employeeDto.salary,
        command.employeeDto.position,
        command.employeeDto.departmentUuid,
    )

    fun updateWith(employee: Employee) = this.apply {
        name = employee.name
        age = employee.age
        salary = employee.salary
        position = employee.position
        departmentUuid = employee.departmentUuid
        deleted = employee.deleted
    }

    fun delete() = this.apply {
        deleted = true
    }

    fun assignProject(projectUuid: String) = this.apply {
        employeeProjects += EmployeeProject(projectUuid = projectUuid)
        registerEvent(ProjectTeamMemberAssignedEvent(projectUuid, uuid))
    }

    fun unassignProject(projectUuid: String) = this.apply {
        employeeProjects = employeeProjects.filter { it.projectUuid != projectUuid }
        registerEvent(ProjectTeamMemberUnassignedEvent(projectUuid, uuid))
    }

    fun moveToDepartment(departmentUuid: String) = this.apply {
        this.departmentUuid = departmentUuid
    }

    fun toDto() = EmployeeDto(
        uuid = uuid,
        name = name,
        age = age,
        salary = salary,
        position = position,
        deleted = deleted,
        version = version,
        departmentUuid = departmentUuid,

        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
    )
}
