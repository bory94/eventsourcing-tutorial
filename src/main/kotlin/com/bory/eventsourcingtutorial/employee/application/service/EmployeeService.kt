package com.bory.eventsourcingtutorial.employee.application.service

import com.bory.eventsourcingtutorial.core.application.dto.EventSourceResponse
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.employee.application.command.*
import com.bory.eventsourcingtutorial.employee.application.event.*
import com.bory.eventsourcingtutorial.employee.domain.Employee
import com.bory.eventsourcingtutorial.employee.domain.exception.NoSuchEmployeeException
import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val eventSourceService: EventSourceService
) {
    fun create(command: CreateEmployeeCommand): EventSourceResponse {
        val created = employeeRepository.save(Employee(UUID.randomUUID().toString(), command))

        return eventSourceService.storeAndGetResponse(
            created.uuid,
            EmployeeCreatedEvent(created)
        )
    }

    fun update(uuid: String, command: UpdateEmployeeCommand): EventSourceResponse {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(uuid)
            ?: throw NoSuchEmployeeException("Employee uuid[$uuid] not found")

        employee.updateWith(Employee(uuid, command))

        return employeeRepository.save(employee)
            .let { saved ->
                eventSourceService.storeAndGetResponse(saved.uuid, EmployeeUpdatedEvent(saved))
            }
    }

    fun delete(command: DeleteEmployeeCommand): EventSourceResponse {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.delete()

        return employeeRepository.save(employee)
            .let {
                eventSourceService.storeAndGetResponse(
                    command.employeeUuid, EmployeeDeletedEvent(command.employeeUuid)
                )
            }
    }

    fun moveToDepartment(command: RequestMoveEmployeeToDepartmentCommand): EventSourceResponse {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        val fromDepartmentUuid = employee.departmentUuid
        employee.moveToDepartment(command.toDepartmentUuid)

        return employeeRepository.save(employee)
            .let {
                eventSourceService.storeAndGetResponse(
                    command.employeeUuid,
                    EmployeeMoveRequestedToDepartmentEvent(
                        command.employeeUuid,
                        fromDepartmentUuid,
                        command.toDepartmentUuid
                    )
                )
            }
    }

    fun assignToProject(command: RequestAssignEmployeeToProjectCommand): EventSourceResponse {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.requestAssignProject(command.projectUuid)

        return employeeRepository.save(employee)
            .let {
                eventSourceService.storeAndGetResponse(
                    employee.uuid,
                    EmployeeAssignRequestedToProjectEvent(employee.uuid, command.projectUuid)
                )
            }
    }

    fun unassignFromProject(command: RequestUnassignEmployeeFromProjectCommand): EventSourceResponse {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.requestUnassignProject(command.projectUuid)

        return employeeRepository.save(employee)
            .let {
                eventSourceService.storeAndGetResponse(
                    employee.uuid,
                    EmployeeUnassignRequestedFromProjectEvent(employee.uuid, command.projectUuid)
                )
            }
    }
}