package com.bory.eventsourcingtutorial.application.command.client

import com.bory.eventsourcingtutorial.application.dto.client.ClientDto
import com.bory.eventsourcingtutorial.application.dto.client.ProjectDto
import javax.validation.Valid

data class CreateClientCommand(
    @field:Valid
    val client: ClientDto,

    @field:Valid
    val projects: List<ProjectDto> = listOf()
)

data class UpdateClientCommand(
    @field:Valid
    val client: ClientDto
)

data class AddProjectsCommand(
    @field:Valid
    val projects: List<ProjectDto> = listOf()
)

data class UpdateProjectCommand(
    @field:Valid
    val project: ProjectDto
)