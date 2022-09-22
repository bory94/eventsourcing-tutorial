package com.bory.eventsourcingtutorial.client.application.command

import com.bory.eventsourcingtutorial.client.application.dto.ProjectDto
import javax.validation.Valid

data class UpdateProjectCommand(
    @field:Valid
    val project: ProjectDto
)