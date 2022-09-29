package com.bory.eventsourcingtutorial.client.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class ClientEventListener(
    private val clientRepository: ClientRepository
) {
    @Async
    @EventListener(classes = [ClientCreatedEvent::class])
    fun on(event: ClientCreatedEvent) {
        retry { clientRepository.save(event.client) }
    }

    @Async
    @EventListener(classes = [ClientUpdatedEvent::class])
    fun on(event: ClientUpdatedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val loadedClient = clientRepository.findByUuidAndDeletedIsFalse(event.client.uuid)
                ?: throw NoSuchClientException("No Such Client[${event.client.uuid}] found, or else deleted uuid inserted")

            loadedClient.updateWith(event.client)

            clientRepository.save(loadedClient)
        }
    }

    @Async
    @EventListener(classes = [ClientDeletedEvent::class])
    fun on(event: ClientDeletedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val loadedClient = clientRepository.findById(event.clientUuid)
                .orElseThrow { throw NoSuchClientException("No Such Client[${event.clientUuid}] found") }

            loadedClient.delete()

            clientRepository.save(loadedClient)
        }
    }
}
