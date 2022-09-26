package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.event.*
import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class ClientProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Client> {
    override fun initialLoad(eventSource: EventSource) =
        objectMapper.readValue(eventSource.payload, ClientCreatedEvent::class.java).client

    override fun processEachEventSource(previous: Client, eventSource: EventSource) =
        when (eventSource.type) {
            ClientUpdatedEvent::class.java.canonicalName -> updateClient(previous, eventSource)
            ClientDeletedEvent::class.java.canonicalName -> deleteClient(previous)
            ProjectsAddedEvent::class.java.canonicalName -> addProjects(previous, eventSource)
            ProjectUpdatedEvent::class.java.canonicalName -> updateProject(previous, eventSource)
            ProjectDeletedEvent::class.java.canonicalName -> deleteProject(previous, eventSource)
            else -> throw java.lang.IllegalArgumentException("Event Type[${eventSource.type}] Not supported")
        }

    private fun updateClient(client: Client, eventSource: EventSource) = client.apply {
        val payloadClient =
            objectMapper.readValue(eventSource.payload!!, ClientUpdatedEvent::class.java).client
        this.updateWith(payloadClient)
    }

    private fun deleteClient(client: Client) = client.delete()

    private fun addProjects(client: Client, eventSource: EventSource): Client = client.apply {
        val newProjects =
            objectMapper.readValue(eventSource.payload!!, ProjectsAddedEvent::class.java).projects

        this.addProject(newProjects)
    }

    private fun updateProject(client: Client, eventSource: EventSource): Client = client.apply {
        val updatingProject =
            objectMapper.readValue(eventSource.payload!!, ProjectUpdatedEvent::class.java).project

        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
        project?.updateWith(updatingProject)
    }

    private fun deleteProject(client: Client, eventSource: EventSource): Client = client.apply {
        val deletingProjectUuid =
            objectMapper.readValue(
                eventSource.payload!!, ProjectDeletedEvent::class.java
            ).projectUuid

        this.removeProject(deletingProjectUuid)
    }
}