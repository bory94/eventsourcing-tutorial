package com.bory.eventsourcingtutorial.client.application.event.listener

import com.bory.eventsourcingtutorial.client.application.event.ProjectDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectUpdatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectsAddedEvent
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchProjectException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProjectEventListener(
    private val clientRepository: ClientRepository
) {
    @Async
    @Transactional
    @EventListener(value = [ProjectsAddedEvent::class])
    fun on(event: ProjectsAddedEvent) {
        retry(times = 3, skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val client = clientRepository.findByUuidAndDeletedIsFalse(event.clientUuid)
                ?: throw NoSuchClientException("No Such Client[${event.clientUuid}] found, or else deleted uuid inserted")

            client.addProject(event.projects)

            clientRepository.save(client)
        }
    }

    @Async
    @Transactional
    @EventListener(value = [ProjectUpdatedEvent::class])
    fun on(event: ProjectUpdatedEvent) {
        retry(
            times = 3,
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
    @Transactional
    @EventListener(classes = [ProjectDeletedEvent::class])
    fun on(event: ProjectDeletedEvent) {
        retry(
            times = 3,
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
}