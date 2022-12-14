package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.core.domain.DomainAggregateRoot
import com.bory.eventsourcingtutorial.employee.application.command.CreateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.command.UpdateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.dto.EmployeeDto
import com.bory.eventsourcingtutorial.employee.application.enum.DepartmentMoveStatus
import com.bory.eventsourcingtutorial.employee.application.enum.EmployeePosition
import com.bory.eventsourcingtutorial.employee.application.enum.ProjectAssignStatus
import com.bory.eventsourcingtutorial.employee.application.event.*
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
    var position: EmployeePosition,
    var departmentUuid: String,
    var departmentMoveStatus: DepartmentMoveStatus = DepartmentMoveStatus.MOVING_ACCEPTED,
    var deleted: Boolean = false,
    version: Int = 0,
    createdAt: Instant? = null,
    updatedAt: Instant? = null,

    @MappedCollection(idColumn = "uuid", keyColumn = "employee_uuid")
    var employeeProjects: List<EmployeeProject> = mutableListOf()
) : DomainAggregateRoot(uuid, version, createdAt, updatedAt) {
    @PersistenceCreator
    constructor(
        uuid: String,

        name: String, age: Int, salary: Int, position: EmployeePosition,
        departmentUuid: String, departmentMoveStatus: DepartmentMoveStatus, deleted: Boolean,

        version: Int,
        createdAt: Instant,
        updatedAt: Instant,
    ) : this(
        uuid,

        name, age, salary, position, departmentUuid, departmentMoveStatus, deleted,

        version,
        createdAt, updatedAt,

        mutableListOf()
    )

    constructor(uuid: String, command: CreateEmployeeCommand) : this(
        uuid,
        command.employee.name,
        command.employee.age,
        command.employee.salary,
        command.employee.position,
        command.employee.departmentUuid,
    ) {
        registerEvent(EmployeeCreatedEvent(this))
        registerEvent(
            EmployeeMoveRequestedToDepartmentEvent(
                uuid,
                null,
                command.employee.departmentUuid
            )
        )
    }

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

        registerEvent(EmployeeUpdatedEvent(this))
    }

    fun delete() = this.apply {
        deleted = true

        registerEvent(EmployeeDeletedEvent(this.uuid))
    }

    fun requestAssignProject(projectUuid: String) = this.apply {
        employeeProjects += EmployeeProject(
            projectUuid = projectUuid,
            status = ProjectAssignStatus.ASSIGNING_REQUESTED
        )

        registerEvent(EmployeeAssignRequestedToProjectEvent(this.uuid, projectUuid))
    }

    fun requestUnassignProject(projectUuid: String) = this.apply {
        val project = employeeProjects.find { it.uuid == projectUuid }
            ?: throw NoSuchProjectException("Project uuid[$uuid] not found.")

        project.status = ProjectAssignStatus.UNASSIGNING_REQUESTED

        registerEvent(EmployeeUnassignRequestedFromProjectEvent(this.uuid, projectUuid))
    }

    fun moveToDepartment(toDepartmentUuid: String) = this.apply {
        val fromDepartmentUuid = this.departmentUuid
        this.departmentUuid = toDepartmentUuid
        this.departmentMoveStatus = DepartmentMoveStatus.MOVING_REQUESTED

        registerEvent(
            EmployeeMoveRequestedToDepartmentEvent(
                this.uuid,
                fromDepartmentUuid,
                toDepartmentUuid
            )
        )
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

    fun moveToDepartmentFailed(fromDepartmentUuid: String) = this.apply {
        this.departmentUuid = fromDepartmentUuid
        this.departmentMoveStatus = DepartmentMoveStatus.MOVING_FAILED
    }

    fun movetoDepartmentAccepted(acceptedDepartmentUuid: String) = this.apply {
        this.departmentUuid = acceptedDepartmentUuid
        this.departmentMoveStatus = DepartmentMoveStatus.MOVING_ACCEPTED
    }
}
