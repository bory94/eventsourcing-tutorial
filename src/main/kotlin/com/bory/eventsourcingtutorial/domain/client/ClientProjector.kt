package com.bory.eventsourcingtutorial.domain.client

import com.bory.eventsourcingtutorial.domain.core.EventSource
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

        return eventSources.subList(1, eventSources.size)
            .fold(initial) { client, eventSource ->
                when (eventSource.type) {
                    "com.bory.eventsourcingtutorial.application.event.client.ClientUpdatedEvent"
                    -> updateClient(client, eventSource)
                    "com.bory.eventsourcingtutorial.application.event.client.ClientDeletedEvent"
                    -> deleteClient(client, eventSource)
                    "com.bory.eventsourcingtutorial.application.event.client.ProjectsAddedEvent"
                    -> addProjects(client, eventSource)
                    "com.bory.eventsourcingtutorial.application.event.client.ProjectUpdatedEvent"
                    -> updateProject(client, eventSource)
                    else -> throw java.lang.IllegalArgumentException("Event Type[${eventSource.type}] Not supported")
                }.apply {
                    version += 1
                    updatedAt = eventSource.createdAt
                }
            }
    }

    fun updateClient(client: Client, eventSource: EventSource) = client.apply {
        val dataClient = objectMapper.readValue(eventSource.payload!!, Client::class.java)
        name = dataClient.name
        address = dataClient.address
        phoneNumber = dataClient.phoneNumber
    }

    fun deleteClient(client: Client, eventSource: EventSource): Client = client.apply {
        deleted = true
    }

    fun addProjects(client: Client, eventSource: EventSource): Client = client.apply {
        val newProjects =
            objectMapper.readValue(
                eventSource.payload!!, object : TypeReference<List<Project>>() {}
            )
        projects += newProjects
    }

    fun updateProject(client: Client, eventSource: EventSource): Client = client.apply {
        val updatingProject = objectMapper.readValue(eventSource.payload!!, Project::class.java)

        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
        if (project != null) {
            project.name = updatingProject.name
            project.description = updatingProject.description
        }
    }
}