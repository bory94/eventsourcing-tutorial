package com.bory.eventsourcingtutorial.client.domain

import com.bory.eventsourcingtutorial.client.application.event.*
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class ClientProjector(
    private val objectMapper: ObjectMapper
) {
    fun project(eventSources: List<EventSource>): Client {
        val initial = objectMapper.readValue(eventSources[0].payload, Client::class.java)
        initial.createdAt = eventSources[0].createdAt
        initial.updatedAt = eventSources[0].createdAt

        return eventSources.subList(1, eventSources.size).fold(initial) { client, eventSource ->
            when (eventSource.type) {
                ClientUpdatedEvent::class.java.canonicalName -> updateClient(client, eventSource)
                ClientDeletedEvent::class.java.canonicalName -> deleteClient(client)
                ProjectsAddedEvent::class.java.canonicalName -> addProjects(client, eventSource)
                ProjectUpdatedEvent::class.java.canonicalName -> updateProject(client, eventSource)
                ProjectDeletedEvent::class.java.canonicalName -> deleteProject(client, eventSource)
                else -> throw java.lang.IllegalArgumentException("Event Type[${eventSource.type}] Not supported")
            }.apply {
                version += 1
                updatedAt = eventSource.createdAt
            }
        }
    }

    private fun updateClient(client: Client, eventSource: EventSource) = client.apply {
        val payloadClient = objectMapper.readValue(eventSource.payload!!, Client::class.java)
        this.updateWith(payloadClient)
    }

    private fun deleteClient(client: Client) = client.deleteClient()

    private fun addProjects(client: Client, eventSource: EventSource): Client = client.apply {
        val newProjects = objectMapper.readValue(
            eventSource.payload!!, object : TypeReference<List<Project>>() {}
        )

        this.addProject(newProjects)
    }

    private fun updateProject(client: Client, eventSource: EventSource): Client = client.apply {
        val updatingProject = objectMapper.readValue(eventSource.payload!!, Project::class.java)

        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
        project?.updateWith(updatingProject)
    }

    private fun deleteProject(client: Client, eventSource: EventSource): Client = client.apply {
        val deletingProjectUuid = eventSource.payload!!
        this.removeProject(deletingProjectUuid)
    }
}