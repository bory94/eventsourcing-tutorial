package com.bory.eventsourcingtutorial.employee.infrastructure.web.command

import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.core.infrastructure.annotations.CommandController
import com.bory.eventsourcingtutorial.core.infrastructure.config.validateAndThrow
import com.bory.eventsourcingtutorial.employee.application.command.*
import com.bory.eventsourcingtutorial.employee.application.event.*
import com.bory.eventsourcingtutorial.employee.domain.Employee
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.Validator

@CommandController
@RequestMapping("/api/v1/employees")
class EmployeeCommandController(
    private val eventSourceService: EventSourceService,
    private val customValidator: Validator
) {
    @PostMapping
    fun create(@RequestBody @Valid command: CreateEmployeeCommand) =
        Employee(UUID.randomUUID().toString(), command)
            .let { eventSourceService.storeAndGetResponse(it.uuid, EmployeeCreatedEvent(it)) }

    @PutMapping("/{uuid}")
    fun update(
        @PathVariable("uuid") uuid: String,
        @RequestBody @Valid command: UpdateEmployeeCommand
    ) = Employee(uuid, command)
        .let {
            eventSourceService.storeAndGetResponse(it.uuid, EmployeeUpdatedEvent(it))
        }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable("uuid") uuid: String) =
        DeleteEmployeeCommand(uuid)
            .also(customValidator::validateAndThrow)
            .let {
                eventSourceService.storeAndGetResponse(uuid, EmployeeDeletedEvent(uuid))
            }

    @PostMapping("/{employeeUuid}/project/{projectUuid}")
    fun assignToProject(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) = AssignEmployeeToProjectCommand(employeeUuid, projectUuid)
        .also(customValidator::validateAndThrow)
        .let {
            eventSourceService.storeAndGetResponse(
                employeeUuid,
                EmployeeAssignedToProjectEvent(employeeUuid, projectUuid)
            )
        }

    @DeleteMapping("/{employeeUuid}/project/{projectUuid}")
    fun unassignFromProject(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("projectUuid") projectUuid: String
    ) = UnassignEmployeeFromProjectCommand(employeeUuid, projectUuid)
        .also(customValidator::validateAndThrow)
        .let {
            eventSourceService.storeAndGetResponse(
                employeeUuid,
                EmployeeUnassignedFromProjectEvent(employeeUuid, projectUuid)
            )
        }

    @PutMapping("/move/{employeeUuid}/department/{departmentUuid}")
    fun moveToDepartment(
        @PathVariable("employeeUuid") employeeUuid: String,
        @PathVariable("departmentUuid") departmentUuid: String
    ) = MoveEmployeeToDepartmentCommand(employeeUuid, departmentUuid)
        .also(customValidator::validateAndThrow)
        .let {
            eventSourceService.storeAndGetResponse(
                employeeUuid,
                EmployeeMovedToDepartmentEvent(employeeUuid, departmentUuid)
            )
        }
}