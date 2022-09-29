package com.bory.eventsourcingtutorial.department.infrastructure.web.command

import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import com.bory.eventsourcingtutorial.department.application.command.CreateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.DeleteDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.UpdateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.event.DepartmentCreatedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentDeletedEvent
import com.bory.eventsourcingtutorial.department.application.event.DepartmentUpdatedEvent
import com.bory.eventsourcingtutorial.department.domain.Department
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/departments")
class DepartmentCommandController(
    private val eventSourceService: EventSourceService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(@RequestBody @Valid command: CreateDepartmentCommand) =
        Department(UUID.randomUUID().toString(), command)
            .let {
                eventSourceService.storeAndGetResponse(it.uuid, DepartmentCreatedEvent(it))
            }

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateDepartmentCommand
    ) =
        Department(uuid, command)
            .let {
                eventSourceService.storeAndGetResponse(it.uuid, DepartmentUpdatedEvent(it))
            }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteDepartmentCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let {
                eventSourceService.storeAndGetResponse(uuid, DepartmentDeletedEvent(uuid))
            }
}