package com.bory.eventsourcingtutorial.client.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class ProjectDeletedEvent(
    val clientUuid: String,
    val projectUuid: String
) : AbstractCustomEvent(aggregateUuid = clientUuid)