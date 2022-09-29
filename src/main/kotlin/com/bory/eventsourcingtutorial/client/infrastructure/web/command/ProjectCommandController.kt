package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.AddProjectsCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteProjectCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateProjectCommand
import com.bory.eventsourcingtutorial.client.application.event.ProjectDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectUpdatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ProjectsAddedEvent
import com.bory.eventsourcingtutorial.client.domain.Project
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/clients/{uuid}/projects")
class ProjectCommandController(
    private val eventSourceService: EventSourceService,
    private val customValidator: Validator
) {
    @PostMapping
    fun addProjects(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody addProjectsCommand: AddProjectsCommand
    ) =
        addProjectsCommand.projects.map { Project(uuid, it) }
            .let {
                eventSourceService.storeAndGetResponse(uuid, ProjectsAddedEvent(uuid, it))
            }

    @PutMapping
    fun updateProject(
        @PathVariable("uuid") uuid: String,
        @Valid @RequestBody updateProjectCommand: UpdateProjectCommand
    ) =
        Project(uuid, updateProjectCommand.project)
            .let {
                eventSourceService.storeAndGetResponse(uuid, ProjectUpdatedEvent(uuid, it))
            }

    @DeleteMapping("/{projectUuid}")
    fun deleteProject(
        @PathVariable("uuid") clientUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) =
        DeleteProjectCommand(clientUuid, projectUuid)
            .also(customValidator::validateAndThrow)
            .let {
                eventSourceService.storeAndGetResponse(
                    clientUuid, ProjectDeletedEvent(clientUuid, projectUuid)
                )
            }

}