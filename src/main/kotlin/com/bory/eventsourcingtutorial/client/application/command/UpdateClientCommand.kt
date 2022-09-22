package com.bory.eventsourcingtutorial.client.application.command

import com.bory.eventsourcingtutorial.client.application.dto.ClientDto
import javax.validation.Valid

data class UpdateClientCommand(
    @field:Valid
    val client: ClientDto
)
