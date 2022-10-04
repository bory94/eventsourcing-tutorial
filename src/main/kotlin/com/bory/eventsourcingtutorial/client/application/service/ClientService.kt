package com.bory.eventsourcingtutorial.client.application.service

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.client.domain.exception.NoSuchClientException
import com.bory.eventsourcingtutorial.client.infrastructure.persistence.ClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ClientService(
    private val clientRepository: ClientRepository
) {
    fun create(command: CreateClientCommand) = clientRepository.save(
        Client(UUID.randomUUID().toString(), command)
    )

    fun update(uuid: String, command: UpdateClientCommand): Client {
        val loadedClient = clientRepository.findByUuidAndDeletedIsFalse(command.client.uuid!!)
            ?: throw NoSuchClientException("No Such Client[${command.client.uuid}] found, or else deleted uuid inserted")

        loadedClient.updateWith(Client(loadedClient.uuid, command))

        return clientRepository.save(loadedClient)
    }

    fun delete(command: DeleteClientCommand): Client {
        val loadedClient = clientRepository.findByUuidAndDeletedIsFalse(command.uuid)
            ?: throw NoSuchClientException("No Such Client[${command.uuid}] found, or else deleted uuid inserted")

        loadedClient.delete()

        return clientRepository.save(loadedClient)
    }
}