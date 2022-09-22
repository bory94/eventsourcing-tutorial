package com.bory.eventsourcingtutorial.client.application.command

import org.hibernate.validator.constraints.Length

data class DeleteProjectCommand(
    @field:Length(min = 36, max = 36)
    val clientUuid: String,
    @field:Length(min = 36, max = 36)
    val projectUuid: String
)
