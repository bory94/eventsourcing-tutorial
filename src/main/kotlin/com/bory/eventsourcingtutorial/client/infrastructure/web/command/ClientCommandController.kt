package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.core.application.dto.EventSourceResponse
import com.bory.eventsourcingtutorial.core.domain.EventSource
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.Validator

@RestController
@RequestMapping("/api/v1/clients")
class ClientCommandController(
    private val eventSourceService: EventSourceService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(
        @RequestBody @Valid command: CreateClientCommand
    ) =
        Client(UUID.randomUUID().toString(), command)
            .let { creatingClient ->
                EventSource(
                    aggregateId = creatingClient.uuid,
                    event = ClientCreatedEvent(creatingClient)
                )
            }
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateClientCommand
    ) =
        EventSource(
            aggregateId = uuid,
            event = ClientUpdatedEvent(Client(uuid, command))
        )
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }


    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteClientCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let {
                EventSource(
                    aggregateId = uuid,
                    event = ClientDeletedEvent(uuid)
                )
            }
            .let(eventSourceService::create)
            .let { EventSourceResponse(it).acceptedResponse() }

}
