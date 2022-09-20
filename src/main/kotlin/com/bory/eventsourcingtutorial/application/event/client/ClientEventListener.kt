package com.bory.eventsourcingtutorial.application.event.client

import com.bory.eventsourcingtutorial.domain.client.NoSuchClientException
import com.bory.eventsourcingtutorial.infrastructure.extensions.retry
import com.bory.eventsourcingtutorial.infrastructure.repository.client.ClientRepository
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

            loadedClient.name = event.client.name
            loadedClient.address = event.client.address
            loadedClient.phoneNumber = event.client.phoneNumber

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

            loadedClient.deleted = true

            clientRepository.save(loadedClient)
        }
    }
}
