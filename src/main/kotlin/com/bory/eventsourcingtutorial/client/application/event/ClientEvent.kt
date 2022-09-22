package com.bory.eventsourcingtutorial.client.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.client.domain.Project

data class ClientCreatedEvent(
    val client: Client
) : AbstractCustomEvent()

data class ClientUpdatedEvent(
    val client: Client
) : AbstractCustomEvent()

data class ClientDeletedEvent(
    val clientUuid: String
) : AbstractCustomEvent()

data class ProjectsAddedEvent(
    val clientUuid: String,
    val projects: List<Project>
) : AbstractCustomEvent()

data class ProjectUpdatedEvent(
    val clientUuid: String,
    val project: Project
) : AbstractCustomEvent()

data class ProjectDeletedEvent(
    val clientUuid: String,
    val projectUuid: String
) : AbstractCustomEvent()