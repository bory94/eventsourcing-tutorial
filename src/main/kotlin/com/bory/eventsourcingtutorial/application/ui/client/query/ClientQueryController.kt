package com.bory.eventsourcingtutorial.application.ui.client.query

import com.bory.eventsourcingtutorial.domain.client.Client
import com.bory.eventsourcingtutorial.domain.client.ClientProjector
import com.bory.eventsourcingtutorial.domain.client.NoSuchClientException
import com.bory.eventsourcingtutorial.infrastructure.repository.client.ClientRepository
import com.bory.eventsourcingtutorial.infrastructure.repository.core.EventSourceRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/v1/clients")
class ClientQueryController(
    private val eventSourceRepository: EventSourceRepository,
    private val clientRepository: ClientRepository,
    private val clientProjector: ClientProjector
) {
    @GetMapping("/{aggregateId}")
    fun projectClient(@PathVariable("aggregateId") aggregateId: String): Client {
        val eventSources = eventSourceRepository.findByAggregateIdOrderByCreatedAt(aggregateId)
        if (eventSources.isEmpty()) throw NoSuchClientException("EventSource for AggregateId[$aggregateId] not found")

        return clientProjector.project(eventSources)
    }

    @GetMapping("/snapshot/{uuid}")
    fun snapshotClient(@PathVariable("uuid") uuid: String): Client =
        clientRepository.findById(uuid)
            .orElseThrow { NoSuchClientException("Client for uuid[$uuid] not found") }

}