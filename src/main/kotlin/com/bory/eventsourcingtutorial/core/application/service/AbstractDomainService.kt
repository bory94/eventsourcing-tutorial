package com.bory.eventsourcingtutorial.core.application.service

import com.bory.eventsourcingtutorial.core.application.event.AbstractCustomEvent
import com.bory.eventsourcingtutorial.core.domain.EventSourceService

abstract class AbstractDomainService(
    private val eventSourceService: EventSourceService
) {
    fun storeEvent(events: Collection<Any>) {
        events.filterIsInstance<AbstractCustomEvent>()
            .forEach(eventSourceService::store)
    }
}