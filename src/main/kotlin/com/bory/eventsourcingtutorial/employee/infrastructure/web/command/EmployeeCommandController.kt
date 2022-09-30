package com.bory.eventsourcingtutorial.employee.infrastructure.web.command

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import com.bory.eventsourcingtutorial.employee.application.command.*
import com.bory.eventsourcingtutorial.employee.application.service.EmployeeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/employees")
class EmployeeCommandController(
    private val employeeService: EmployeeService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(@RequestBody @Valid command: CreateEmployeeCommand) =
        ResponseEntity.accepted().body(
            employeeService.create(command)
        )

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateEmployeeCommand
    ) =
        ResponseEntity.accepted().body(
            employeeService.update(uuid, command)
        )

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteEmployeeCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let(employeeService::delete)
            .let(ResponseEntity.accepted()::body)

    @PutMapping("/move/{employeeUuid}/department/{departmentUuid}")
    fun moveToDepartment(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("departmentUuid") departmentUuid: String
    ) = RequestMoveEmployeeToDepartmentCommand(employeeUuid, departmentUuid)
        .also(customValidator::validateAndThrow)
        .let(employeeService::moveToDepartment)
        .let(ResponseEntity.accepted()::body)

    @PostMapping("/{employeeUuid}/project/{projectUuid}")
    fun assignToProject(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) = RequestAssignEmployeeToProjectCommand(employeeUuid, projectUuid)
        .also(customValidator::validateAndThrow)
        .let(employeeService::assignToProject)
        .let(ResponseEntity.accepted()::body)

    @DeleteMapping("/{employeeUuid}/project/{projectUuid}")
    fun unassignFromProject(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) = RequestUnassignEmployeeFromProjectCommand(employeeUuid, projectUuid)
        .also(customValidator::validateAndThrow)
        .let(employeeService::unassignFromProject)
        .let(ResponseEntity.accepted()::body)
}