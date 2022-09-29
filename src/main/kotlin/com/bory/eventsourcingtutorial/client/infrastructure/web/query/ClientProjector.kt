package com.bory.eventsourcingtutorial.client.infrastructure.web.query

import com.bory.eventsourcingtutorial.client.application.event.*
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.core.domain.AggregateRootProjector
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainProjector
import com.fasterxml.jackson.databind.ObjectMapper

@DomainProjector
class ClientProjector(
    private val objectMapper: ObjectMapper
) : AggregateRootProjector<Client> {
    override fun initialLoad(eventSource: EventSource) =
        objectMapper.readValue(eventSource.payload, ClientCreatedEvent::class.java).client

    override fun eventCases(): Map<Class<out Any>, (Client, EventSource) -> Client> = mapOf(
        ClientUpdatedEvent::class.java to this::updateClient,
        ClientDeletedEvent::class.java to { client, _ -> client.delete() },
        ProjectsAddedEvent::class.java to this::addProjects,
        ProjectUpdatedEvent::class.java to this::updateProject,
        ProjectDeletedEvent::class.java to this::deleteProject,
        ProjectTeamMemberAssignedEvent::class.java to this::assignTeamMember,
        ProjectTeamMemberUnassignedEvent::class.java to this::unassignTeamMember
    )

    private fun updateClient(client: Client, eventSource: EventSource) = client.apply {
        val payloadClient =
            objectMapper.readValue(eventSource.payload!!, ClientUpdatedEvent::class.java).client
        this.updateWith(payloadClient)
    }

    private fun addProjects(client: Client, eventSource: EventSource) = client.apply {
        val newProjects =
            objectMapper.readValue(eventSource.payload!!, ProjectsAddedEvent::class.java).projects

        this.addProject(newProjects)
    }

    private fun updateProject(client: Client, eventSource: EventSource) = client.apply {
        val updatingProject =
            objectMapper.readValue(eventSource.payload!!, ProjectUpdatedEvent::class.java).project

        val project = projects.firstOrNull { it.uuid == updatingProject.uuid }
        project?.updateWith(updatingProject)
    }

    private fun deleteProject(client: Client, eventSource: EventSource) = client.apply {
        val deletingProjectUuid =
            objectMapper.readValue(
                eventSource.payload!!,
                ProjectDeletedEvent::class.java
            ).projectUuid

        removeProject(deletingProjectUuid)
    }

    private fun assignTeamMember(client: Client, eventSource: EventSource) = client.apply {
        val event = objectMapper.readValue(
            eventSource.payload!!,
            ProjectTeamMemberAssignedEvent::class.java
        )

        assignProjectTeamMember(event.projectUuid, event.teamMemberUuid)
    }

    private fun unassignTeamMember(client: Client, eventSource: EventSource) = client.apply {
        val event = objectMapper.readValue(
            eventSource.payload!!,
            ProjectTeamMemberUnassignedEvent::class.java
        )

        unassignProjectTeamMember(event.projectUuid, event.teamMemberUuid)
    }
}