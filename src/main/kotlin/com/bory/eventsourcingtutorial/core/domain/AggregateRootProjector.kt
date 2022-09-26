package com.bory.eventsourcingtutorial.core.domain

interface AggregateRootProjector<T : AbstractPersistableAggregateRoot> {
    fun initialLoad(eventSource: EventSource): T

    fun project(eventSources: List<EventSource>): T {
        val initial = initialLoad(eventSources[0]).apply {
            createdAt = eventSources[0].createdAt
            updatedAt = eventSources[0].updatedAt
        }
        if (eventSources.size == 1) return initial

        return eventSources.subList(1, eventSources.size).fold(initial) { previous, eventSource ->
            processEachEventSource(previous, eventSource).apply {
                updatedAt = eventSource.updatedAt
                version += 1
            }
        }
    }

    fun processEachEventSource(previous: T, eventSource: EventSource): T
}