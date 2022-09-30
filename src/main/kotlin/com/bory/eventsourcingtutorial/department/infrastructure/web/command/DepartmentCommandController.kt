package com.bory.eventsourcingtutorial.department.infrastructure.web.command

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import com.bory.eventsourcingtutorial.department.application.command.CreateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.DeleteDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.command.UpdateDepartmentCommand
import com.bory.eventsourcingtutorial.department.application.service.DepartmentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/departments")
class DepartmentCommandController(
    private val departmentService: DepartmentService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(@RequestBody @Valid command: CreateDepartmentCommand) =
        ResponseEntity.accepted().body(
            departmentService.create(command)
        )

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateDepartmentCommand
    ) =
        ResponseEntity.accepted().body(
            departmentService.update(uuid, command)
        )

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteDepartmentCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let(departmentService::delete)
            .let(ResponseEntity.accepted()::body)
}