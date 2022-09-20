package com.bory.eventsourcingtutorial.application.event.client

import com.bory.eventsourcingtutorial.domain.client.NoSuchClientException
import com.bory.eventsourcingtutorial.domain.client.NoSuchProjectException
import com.bory.eventsourcingtutorial.infrastructure.extensions.retry
import com.bory.eventsourcingtutorial.infrastructure.repository.client.ClientRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

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

            val project = client.projects.firstOrNull { it.uuid == event.project.uuid }
                ?: throw NoSuchClientException("No Such Project[${event.project.uuid}] found, or else deleted uuid inserted")
            project.name = event.project.name
            project.description = event.project.description
            project.updatedAt = Instant.now()

            clientRepository.save(client)
        }
    }
}