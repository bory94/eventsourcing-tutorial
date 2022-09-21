package com.bory.eventsourcingtutorial.domain.client

import com.bory.eventsourcingtutorial.application.command.client.CreateClientCommand
import com.bory.eventsourcingtutorial.application.command.client.UpdateClientCommand
import com.bory.eventsourcingtutorial.application.dto.client.ProjectDto
import com.bory.eventsourcingtutorial.domain.core.AbstractPersistableAggregateRoot
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("client")
class Client(
    uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var phoneNumber: String,
    var address: String,
    var deleted: Boolean = false,
    version: Int = 1,
    createdAt: Instant? = null,
    @field:LastModifiedDate
    var updatedAt: Instant? = null,

    @MappedCollection(idColumn = "client_uuid", keyColumn = "uuid")
    var projects: List<Project> = mutableListOf(),

    persisted: Boolean = false
) : AbstractPersistableAggregateRoot(uuid, version, createdAt, persisted) {

    @PersistenceCreator
    constructor(
        uuid: String,
        name: String, phoneNumber: String, address: String, deleted: Boolean,
        version: Int,
        createdAt: Instant, updatedAt: Instant
    ) : this(
        uuid,
        name, phoneNumber, address, deleted,
        version,
        createdAt, updatedAt,
        mutableListOf(),
        true
    )

    constructor(uuid: String, command: CreateClientCommand) : this(
        uuid = uuid,
        name = command.client.name,
        phoneNumber = command.client.phoneNumber,
        address = command.client.address,
        projects = command.projects.map {
            Project(
                name = it.name,
                description = it.description,
                clientUuid = uuid
            )
        }
    )

    constructor(uuid: String, command: UpdateClientCommand) : this(
        uuid = uuid,
        name = command.client.name,
        phoneNumber = command.client.phoneNumber,
        address = command.client.address
    )

    fun addProject(projects: List<Project>) {
        this.projects += projects
    }

    override fun toString(): String {
        return """
            ${super.toString()}
            Client(name='$name', phoneNumber='$phoneNumber', address='$address', updatedAt=$updatedAt, projects=$projects)
            """
    }
}

@Table("project")
data class Project(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
    val clientUuid: String
) {
    constructor(clientUuid: String, projectDto: ProjectDto) : this(
        uuid = projectDto.uuid ?: UUID.randomUUID().toString(),
        name = projectDto.name,
        description = projectDto.description,
        clientUuid = clientUuid
    )
}