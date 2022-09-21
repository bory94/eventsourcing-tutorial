package com.bory.eventsourcingtutorial.application.ui.client.command

import com.bory.eventsourcingtutorial.application.command.client.AddProjectsCommand
import com.bory.eventsourcingtutorial.application.command.client.UpdateProjectCommand
import com.bory.eventsourcingtutorial.application.dto.core.EventSourceResponse
import com.bory.eventsourcingtutorial.application.event.client.ProjectDeletedEvent
import com.bory.eventsourcingtutorial.application.event.client.ProjectUpdatedEvent
import com.bory.eventsourcingtutorial.application.event.client.ProjectsAddedEvent
import com.bory.eventsourcingtutorial.domain.client.Project
import com.bory.eventsourcingtutorial.domain.core.EventSource
import com.bory.eventsourcingtutorial.domain.core.EventSourceService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.ValidationException

@RestController
@RequestMapping("/api/v1/clients/{uuid}/projects")
class ProjectCommandController(
    private val objectMapper: ObjectMapper,
    private val eventSourceService: EventSourceService
) {
    @PostMapping
    fun addProjects(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody addProjectsCommand: AddProjectsCommand
    ): ResponseEntity<EventSourceResponse> {
        val projects = addProjectsCommand.projects.map { Project(uuid, it) }

        return EventSource(
            aggregateId = uuid,
            payload = objectMapper.writeValueAsString(projects),
            event = ProjectsAddedEvent(uuid, projects)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }

    }

    @PutMapping
    fun updateProject(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody updateProjectCommand: UpdateProjectCommand
    ): ResponseEntity<EventSourceResponse> {
        val project = Project(uuid, updateProjectCommand.project)

        return EventSource(
            aggregateId = uuid,
            payload = objectMapper.writeValueAsString(project),
            event = ProjectUpdatedEvent(uuid, project)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }
    }

    @DeleteMapping("/{projectUuid}")
    fun deleteProject(
        @PathVariable("uuid") clientUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) {
        if (clientUuid.length != 36) throw ValidationException("Invalid Client UUID: $clientUuid")
        if (projectUuid.length != 36) throw ValidationException("Invalid Project UUID: $projectUuid")

        return EventSource(
            aggregateId = clientUuid,
            payload = projectUuid,
            event = ProjectDeletedEvent(clientUuid, projectUuid)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }
    }
}