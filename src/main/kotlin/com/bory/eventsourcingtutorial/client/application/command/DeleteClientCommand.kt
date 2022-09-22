package com.bory.eventsourcingtutorial.client.application.command

import org.hibernate.validator.constraints.Length

data class DeleteClientCommand(
    @field:Length(min = 36, max = 36)
    val uuid: String
)