package com.bory.eventsourcingtutorial.client.application.event

import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent

data class ClientCreatedEvent(
    val client: Client
) : AbstractCustomEvent(aggregateUuid = client.uuid)