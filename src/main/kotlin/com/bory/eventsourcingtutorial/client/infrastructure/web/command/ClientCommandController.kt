package com.bory.eventsourcingtutorial.client.infrastructure.web.command

import com.bory.eventsourcingtutorial.client.application.command.CreateClientCommand
import com.bory.eventsourcingtutorial.client.application.command.DeleteClientCommand
import com.bory.eventsourcingtutorial.client.application.command.UpdateClientCommand
import com.bory.eventsourcingtutorial.client.application.service.ClientService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/clients")
class ClientCommandController(
    private val clientService: ClientService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(
        @RequestBody @Valid command: CreateClientCommand
    ) =
        ResponseEntity.accepted().body(clientService.create(command))

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateClientCommand
    ) =
        ResponseEntity.accepted().body(clientService.update(uuid, command))

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteClientCommand(uuid)
            .let(customValidator::validateAndThrow)
            .let(clientService::delete)
            .let(ResponseEntity.accepted()::body)

}
