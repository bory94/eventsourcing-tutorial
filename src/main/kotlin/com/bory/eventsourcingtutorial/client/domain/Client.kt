package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.core.domain.AbstractPersistableAggregateRoot
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

    fun updateClient(updatingClient: Client) {
        name = updatingClient.name
        address = updatingClient.address
        phoneNumber = updatingClient.phoneNumber
    }

    fun deleteClient() {
        this.deleted = true
    }

    fun addProject(projects: List<Project>) {
        this.projects += projects
    }

    fun updateProject(updatingProject: Project) {
        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
            ?: throw NoSuchProjectException("No Such Project[${updatingProject.uuid}] found")
        project.updateWith(updatingProject)
    }

    fun removeProject(projectUuid: String) {
        projects -= projects.find { it.uuid == projectUuid }
            ?: throw NoSuchProjectException("No Such Project[$projectUuid] found")
    }

    override fun toString(): String {
        return """
            ${super.toString()}
            Client(name='$name', phoneNumber='$phoneNumber', address='$address', updatedAt=$updatedAt, projects=$projects)
            """
    }
}
