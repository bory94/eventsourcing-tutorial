package com.bory.eventsourcingtutorial.client.application.command

import com.bory.eventsourcingtutorial.client.application.dto.ClientDto
import com.bory.eventsourcingtutorial.client.application.dto.ProjectDto
import javax.validation.Valid

data class CreateClientCommand(
    @field:Valid
    val client: ClientDto,

    @field:Valid
    val projects: List<ProjectDto> = listOf()
)
