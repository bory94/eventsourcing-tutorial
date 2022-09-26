package com.bory.eventsourcingtutorial.core.domain

interface AggregateRootProjector<T : AbstractPersistableAggregateRoot> {
    fun initialLoad(eventSource: EventSource): T
    fun eventCases(): Map<Class<out Any>, (T, EventSource) -> T>

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

    private fun processEachEventSource(previous: T, eventSource: EventSource): T {
        val clazz = try {
            Class.forName(eventSource.type)
        } catch (e: Exception) {
            throw java.lang.IllegalArgumentException("Invalid Event Type[${eventSource.type}] stored in Event Store")
        }

        val process = eventCases()[clazz]
            ?: throw java.lang.IllegalArgumentException("Event Type[${eventSource.type}] Not Supported")

        return process.invoke(previous, eventSource)
    }
}