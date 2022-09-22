package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.core.application.dto.EventSourceResponse
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.ValidationException

@RestController
@RequestMapping("/api/v1/clients")
class ClientCommandController(
    private val objectMapper: ObjectMapper,
    private val eventSourceService: EventSourceService
) {
    @PostMapping
    fun create(@RequestBody @Valid command: CreateClientCommand): ResponseEntity<EventSourceResponse> {
        val creatingClient = Client(UUID.randomUUID().toString(), command)

        return EventSource(
            aggregateId = creatingClient.uuid,
            payload = objectMapper.writeValueAsString(creatingClient),
            event = ClientCreatedEvent(creatingClient)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }
    }

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateClientCommand
    ): ResponseEntity<EventSourceResponse> {
        val updatingClient = Client(uuid, command)

        return EventSource(
            aggregateId = uuid,
            payload = objectMapper.writeValueAsString(updatingClient),
            event = ClientUpdatedEvent(updatingClient)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String): ResponseEntity<EventSourceResponse> {
        if (uuid.length != 36) {
            throw ValidationException("Invalid Client UUID: $uuid")
        }

        return EventSource(
            aggregateId = uuid,
            event = ClientDeletedEvent(uuid)
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }
    }

}
