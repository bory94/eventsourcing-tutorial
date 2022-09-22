package com.bory.eventsourcingtutorial.client.application.command

import com.bory.eventsourcingtutorial.client.application.dto.ProjectDto
import javax.validation.Valid

data class AddProjectsCommand(
    @field:Valid
    val projects: List<ProjectDto> = listOf()
)