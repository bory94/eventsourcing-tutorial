package com.bory.eventsourcingtutorial.client.application.event

data class ProjectTeamMemberAssignedEvent(
    val projectUuid: String,
    val teamMemberUuid: String
)
