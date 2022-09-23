package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.AddProjectsCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteProjectCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateProjectCommand
import com.bory.eventsourcingtutorial.client.application.event.ProjectDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectUpdatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectsAddedEvent
import com.bory.eventsourcingtutorial.client.domain.Project
import com.bory.eventsourcingtutorial.core.application.dto.EventSourceResponse
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@RestController
@RequestMapping("/api/v1/clients/{uuid}/projects")
class ProjectCommandController(
    private val objectMapper: ObjectMapper,
    private val eventSourceService: EventSourceService,
    private val customValidator: Validator
) {
    @PostMapping
    fun addProjects(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody addProjectsCommand: AddProjectsCommand
    ) =
        addProjectsCommand.projects.map { Project(uuid, it) }
            .let { projects ->
                EventSource(
                    aggregateId = uuid,
                    payload = objectMapper.writeValueAsString(projects),
                    event = ProjectsAddedEvent(uuid, projects)
                )
            }
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }


    @PutMapping
    fun updateProject(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody updateProjectCommand: UpdateProjectCommand
    ) =
        Project(uuid, updateProjectCommand.project)
            .let { project ->
                EventSource(
                    aggregateId = uuid,
                    payload = objectMapper.writeValueAsString(project),
                    event = ProjectUpdatedEvent(uuid, project)
                )
            }
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }

    @DeleteMapping("/{projectUuid}")
    fun deleteProject(
        @PathVariable("uuid") clientUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) =
        DeleteProjectCommand(clientUuid, projectUuid)
            .also(customValidator::validateAndThrow)
            .let {
                EventSource(
                    aggregateId = clientUuid,
                    payload = projectUuid,
                    event = ProjectDeletedEvent(clientUuid, projectUuid)
                )
            }
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }

}