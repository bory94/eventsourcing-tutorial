package com.bory.eventsourcingtutorial.client.application.event.listener

import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ClientEventListener(
    private val clientRepository: ClientRepository
) {
    @Async
    @Transactional
    @EventListener(classes = [ClientCreatedEvent::class])
    fun on(event: ClientCreatedEvent) {
        retry(times = 3) { clientRepository.save(event.client) }
    }

    @Async
    @Transactional
    @EventListener(classes = [ClientUpdatedEvent::class])
    fun on(event: ClientUpdatedEvent) {
        retry(times = 3, skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val loadedClient = clientRepository.findByUuidAndDeletedIsFalse(event.client.uuid)
                ?: throw NoSuchClientException("No Such Client[${event.client.uuid}] found, or else deleted uuid inserted")

            loadedClient.updateWith(event.client)

            clientRepository.save(loadedClient)
        }
    }

    @Async
    @Transactional
    @EventListener(classes = [ClientDeletedEvent::class])
    fun on(event: ClientDeletedEvent) {
        retry(times = 3, skipRetryExceptions = arrayOf(NoSuchClientException::class.java)) {
            val loadedClient = clientRepository.findById(event.clientUuid)
                .orElseThrow { throw NoSuchClientException("No Such Client[${event.clientUuid}] found") }

            loadedClient.deleteClient()

            clientRepository.save(loadedClient)
        }
    }
}
