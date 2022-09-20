package com.bory.eventsourcingtutorial.application.ui.client.command

import com.bory.eventsourcingtutorial.application.command.client.CreateClientCommand
import com.bory.eventsourcingtutorial.application.command.client.UpdateClientCommand
import com.bory.eventsourcingtutorial.application.dto.core.EventSourceResponse
import com.bory.eventsourcingtutorial.application.event.client.ClientCreatedEvent
import com.bory.eventsourcingtutorial.application.event.client.ClientDeletedEvent
import com.bory.eventsourcingtutorial.application.event.client.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.domain.client.Client
import com.bory.eventsourcingtutorial.domain.core.EventSource
import com.bory.eventsourcingtutorial.domain.core.EventSourceService
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
