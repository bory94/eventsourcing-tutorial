package com.bory.eventsourcingtutorial.client.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.client.application.event.*
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ProjectRepository
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class ProjectEventListener(
    private val clientRepository: ClientRepository,
    private val projectRepository: ProjectRepository,
    private val eventSourceService: EventSourceService
) {
    @Async
    @EventListener(value = [ProjectsAddedEvent::class])
    fun on(event: ProjectsAddedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val client = clientRepository.findByUuidAndDeletedIsFalse(event.clientUuid)
                ?: throw NoSuchClientException("No Such Client[${event.clientUuid}] found, or else deleted uuid inserted")

            client.addProject(event.projects)

            clientRepository.save(client)
        }
    }

    @Async
    @EventListener(value = [ProjectUpdatedEvent::class])
    fun on(event: ProjectUpdatedEvent) {
        retry(
            skipRetryExceptions = arrayOf(
                NoSuchClientException::class.java,
                NoSuchProjectException::class.java
            )
        ) {
            val client = clientRepository.findByUuidAndDeletedIsFalse(event.clientUuid)
                ?: throw NoSuchClientException("No Such Client[${event.clientUuid}] found, or else deleted uuid inserted")

            client.updateProject(event.project)

            clientRepository.save(client)
        }
    }

    @Async
    @EventListener(classes = [ProjectDeletedEvent::class])
    fun on(event: ProjectDeletedEvent) {
        retry(
            skipRetryExceptions = arrayOf(
                NoSuchClientException::class.java,
                NoSuchProjectException::class.java
            )
        ) {
            val loadedClient = clientRepository.findByUuidAndDeletedIsFalse(event.clientUuid)
                ?: throw NoSuchClientException("No Such Client[${event.clientUuid}] found")

            loadedClient.removeProject(event.projectUuid)

            clientRepository.save(loadedClient)
        }
    }

    @Async
    @EventListener(classes = [ProjectTeamMemberAssignedEvent::class])
    fun on(event: ProjectTeamMemberAssignedEvent) {
        val client = findClient(event.projectUuid)
        client.assignProjectTeamMember(event.projectUuid, event.teamMemberUuid)

        clientRepository.save(client)

        eventSourceService.storeAndGetResponse(client.uuid, event)
    }

    @Async
    @EventListener(classes = [ProjectTeamMemberUnassignedEvent::class])
    fun on(event: ProjectTeamMemberUnassignedEvent) {
        val client = findClient(event.projectUuid)
        client.unassignProjectTeamMember(event.projectUuid, event.teamMemberUuid)

        clientRepository.save(client)

        eventSourceService.storeAndGetResponse(client.uuid, event)
    }

    private fun findClient(projectUuid: String): Client {
        val clientUuid = projectRepository.findClientUuidByProjectUuid(projectUuid)
            ?: throw NoSuchProjectException("Project uuid[$projectUuid not found.")

        return clientRepository.findByUuidAndDeletedIsFalse(clientUuid)
            ?: throw NoSuchClientException("Client uuid[$clientUuid] not found.")
    }
}