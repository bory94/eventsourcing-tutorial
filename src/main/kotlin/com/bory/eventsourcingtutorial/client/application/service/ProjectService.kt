package com.bory.eventsourcingtutorial.client.application.service

import com.bory.eventsourcingtutorial.client.application.command.AddProjectsCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteProjectCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateProjectCommand
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.client.domain.Project
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectService(
    private val clientRepository: ClientRepository
) {
    fun addProjectsTo(clientUuid: String, command: AddProjectsCommand): Client {
        val client = clientRepository.findByUuidAndDeletedIsFalse(clientUuid)
            ?: throw NoSuchClientException("No Such Client[${clientUuid}] found, or else deleted uuid inserted")

        val addingProjects = command.projects.map { Project(client.uuid, it) }
        client.addProject(addingProjects)

        return clientRepository.save(client)
    }

    fun update(clientUuid: String, command: UpdateProjectCommand): Client {
        val client = clientRepository.findByUuidAndDeletedIsFalse(clientUuid)
            ?: throw NoSuchClientException("No Such Client[${clientUuid}] found, or else deleted uuid inserted")

        client.updateProject(Project(client.uuid, command.project))

        return clientRepository.save(client)
    }

    fun delete(clientUuid: String, command: DeleteProjectCommand): Client {
        val client = clientRepository.findByUuidAndDeletedIsFalse(clientUuid)
            ?: throw NoSuchClientException("No Such Client[${clientUuid}] found, or else deleted uuid inserted")

        client.deleteProject(command.projectUuid)

        return clientRepository.save(client)
    }
}