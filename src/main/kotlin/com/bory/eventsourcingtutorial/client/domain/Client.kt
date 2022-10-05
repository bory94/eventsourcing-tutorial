package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.application.dto.ClientDto
import com.bory.eventsourcingtutorial.client.application.event.*
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.core.domain.DomainAggregateRoot
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
    version: Int = 0,
    createdAt: Instant? = null,
    updatedAt: Instant? = null,

    @MappedCollection(idColumn = "client_uuid", keyColumn = "uuid")
    var projects: List<Project> = mutableListOf(),
) : DomainAggregateRoot(uuid, version, createdAt, updatedAt) {

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
        mutableListOf()
    )

    constructor(uuid: String, command: CreateClientCommand) : this(
        uuid = uuid,
        name = command.client.name,
        phoneNumber = command.client.phoneNumber,
        address = command.client.address,
        projects = command.client.projects.map {
            Project(
                name = it.name,
                description = it.description,
                clientUuid = uuid
            )
        }
    ) {
        registerEvent(ClientCreatedEvent(this))
    }

    constructor(uuid: String, command: UpdateClientCommand) : this(
        uuid = uuid,
        name = command.client.name,
        phoneNumber = command.client.phoneNumber,
        address = command.client.address
    )

    fun updateWith(updating: Client) = this.apply {
        name = updating.name
        address = updating.address
        phoneNumber = updating.phoneNumber

        registerEvent(ClientUpdatedEvent(this))
    }

    fun delete() = this.apply {
        this.deleted = true

        registerEvent(ClientDeletedEvent(this.uuid))
    }

    fun addProject(projects: List<Project>) = this.apply {
        this.projects += projects

        registerEvent(ProjectsAddedEvent(this.uuid, projects))
    }

    fun updateProject(updatingProject: Project) = this.apply {
        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
            ?: throw NoSuchProjectException("Project uuid[${updatingProject.uuid}] not found.")
        project.updateWith(updatingProject)

        registerEvent(ProjectUpdatedEvent(this.uuid, updatingProject))
    }

    fun deleteProject(projectUuid: String) = this.apply {
        projects -= projects.find { it.uuid == projectUuid }
            ?: throw NoSuchProjectException("Project uuid[$projectUuid] not found.")

        registerEvent(ProjectDeletedEvent(this.uuid, projectUuid))
    }

    fun toDto() = ClientDto(
        uuid = uuid,
        name = name,
        phoneNumber = phoneNumber,
        address = address,
        deleted = deleted,
        version = version,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
        projects = projects.map(Project::toDto)

    )

    override fun toString(): String {
        return """
            ${super.toString()}
            Client(name='$name', phoneNumber='$phoneNumber', address='$address', updatedAt=$updatedAt, projects=$projects)
            """
    }
}
