package com.bory.eventsourcingtutorial.employee.domain

import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
import com.bory.eventsourcingtutorial.employee.application.command.CreateEmployeeCommand
import com.bory.eventsourcingtutorial.employee.application.command.UpdateEmployeeCommand
import org.springframework.data.annotation.PersistenceCreator
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

    persisted: Boolean = false
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, updatedAt, persisted) {
    @PersistenceCreator
    constructor(
        uuid: String,

        name: String, age: Int, salary: Int, position: String,
        departmentUuid: String, deleted: Boolean,

        version: Int,
        createdAt: Instant,
        updatedAt: Instant
    ) : this(
        uuid,

        name, age, salary, position, departmentUuid, deleted,

        version,
        createdAt, updatedAt,
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
}