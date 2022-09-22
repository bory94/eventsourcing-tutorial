package com.bory.eventsourcingtutorial.client.application.event

import com.bory.eventsourcingtutorial.client.domain.Project
import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class ProjectsAddedEvent(
    val clientUuid: String,
    val projects: List<Project>
) : AbstractCustomEvent()