package com.bory.eventsourcingtutorial.department.domain

import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
import com.bory.eventsourcingtutorial.department.application.command.CreateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.UpdateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.dto.DepartmentDto
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("department")
class Department(
    uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var deleted: Boolean = false,
    version: Int = 1,
    createdAt: Instant? = null,
    updatedAt: Instant? = null,

    persisted: Boolean = false
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, updatedAt, persisted) {
    @PersistenceCreator
    constructor(
        uuid: String,
        name: String, description: String, deleted: Boolean,
        version: Int,
        createdAt: Instant,
        updatedAt: Instant
    ) : this(
        uuid,
        name, description, deleted,
        version,
        createdAt, updatedAt,
        true
    )

    constructor(uuid: String, command: CreateDepartmentCommand) : this(
        uuid = uuid,
        name = command.departmentDto.name,
        description = command.departmentDto.name,
        deleted = command.departmentDto.deleted
    )

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
    }

    fun delete() = this.apply {
        deleted = true
    }

    fun toDto() = DepartmentDto(
        uuid = uuid,
        name = name,
        description = description,
        deleted = deleted,
        version = version,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!
    )
}