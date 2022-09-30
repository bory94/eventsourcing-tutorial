package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.AddProjectsCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteProjectCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateProjectCommand
import com.bory.eventsourcingtutorial.client.application.service.ProjectService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/clients/{clientUuid}/projects")
class ProjectCommandController(
    private val projectService: ProjectService,
    private val customValidator: Validator
) {
    @PostMapping
    fun addProjects(
        @PathVariable("clientUuid") clientUuid: String,
        @Valid @RequestBody command: AddProjectsCommand
    ) =
        ResponseEntity.accepted().body(
            projectService.addProjectsTo(clientUuid, command)
        )

    @PutMapping
    fun updateProject(
        @PathVariable("clientUuid") clientUuid: String,
        @Valid @RequestBody command: UpdateProjectCommand
    ) =
        ResponseEntity.accepted().body(
            projectService.update(clientUuid, command)
        )

    @DeleteMapping("/{projectUuid}")
    fun deleteProject(
        @PathVariable("clientUuid") clientUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) =
        DeleteProjectCommand(clientUuid, projectUuid)
            .also(customValidator::validateAndThrow)
            .let {
                projectService.delete(clientUuid, it)
            }

}