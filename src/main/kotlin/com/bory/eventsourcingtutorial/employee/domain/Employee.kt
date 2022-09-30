package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
import com.bory.eventsourcingtutorial.employee.application.command.CreateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.command.UpdateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.dto.EmployeeDto
import com.bory.eventsourcingtutorial.employee.application.enum.DepartmentMoveStatus
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
    var position: String,
    var departmentUuid: String,
    var departmentMoveStatus: DepartmentMoveStatus = DepartmentMoveStatus.MOVING_ACCEPTED,
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
        departmentUuid: String, departmentMoveStatus: DepartmentMoveStatus, deleted: Boolean,

        version: Int,
        createdAt: Instant,
        updatedAt: Instant,
    ) : this(
        uuid,

        name, age, salary, position, departmentUuid, departmentMoveStatus, deleted,

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
    ) {
        registerEvent(EmployeeCreatedEvent(this))
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
}
