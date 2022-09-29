package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.application.event.ClientCreatedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientDeletedEvent
import com.bory.eventsourcingtutorial.client.application.event.ClientUpdatedEvent
import com.bory.eventsourcingtutorial.client.domain.Client
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
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
            .let {
                eventSourceService.storeAndGetResponse(it.uuid, ClientCreatedEvent(it))
            }

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateClientCommand
    ) =
        Client(uuid, command).let {
            eventSourceService.storeAndGetResponse(uuid, ClientUpdatedEvent(it))
        }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteClientCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let {
                eventSourceService.storeAndGetResponse(uuid, ClientDeletedEvent(uuid))
            }

}
