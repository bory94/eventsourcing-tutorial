package com.bory.eventsourcingtutorial.client.application.event

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class ClientDeletedEvent(
    val clientUuid: String
) : AbstractCustomEvent(aggregateUuid = clientUuid)