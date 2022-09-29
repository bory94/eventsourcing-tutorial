package com.bory.eventsourcingtutorial.client.application.event

data class ProjectTeamMemberUnassignedEvent(
    val projectUuid: String,
    val teamMemberUuid: String
)
