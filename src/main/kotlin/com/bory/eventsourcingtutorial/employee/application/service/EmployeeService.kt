package com.bory.eventsourcingtutorial.employee.application.service

import com.bory.eventsourcingtutorial.core.application.service.AbstractDomainService
import com.bory.eventsourcingtutorial.core.domain.EventSourceService
import com.bory.eventsourcingtutorial.employee.application.command.*
import com.bory.eventsourcingtutorial.employee.domain.Employee
import com.bory.eventsourcingtutorial.employee.domain.exception.NoSuchEmployeeException
import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    eventSourceService: EventSourceService
) : AbstractDomainService(eventSourceService) {
    fun create(command: CreateEmployeeCommand): Employee =
        employeeRepository.save(Employee(UUID.randomUUID().toString(), command))
            .apply { storeEvent(registeredEvents()) }


    fun update(uuid: String, command: UpdateEmployeeCommand): Employee {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(uuid)
            ?: throw NoSuchEmployeeException("Employee uuid[$uuid] not found")

        employee.updateWith(Employee(uuid, command))

        return employeeRepository.save(employee).apply { storeEvent(registeredEvents()) }
    }

    fun delete(command: DeleteEmployeeCommand): Employee {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.delete()

        return employeeRepository.save(employee).apply { storeEvent(registeredEvents()) }
    }

    fun moveToDepartment(command: RequestMoveEmployeeToDepartmentCommand): Employee {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.moveToDepartment(command.toDepartmentUuid)

        return employeeRepository.save(employee).apply { storeEvent(registeredEvents()) }
    }

    fun assignToProject(command: RequestAssignEmployeeToProjectCommand): Employee {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.requestAssignProject(command.projectUuid)

        return employeeRepository.save(employee).apply { storeEvent(registeredEvents()) }
    }

    fun unassignFromProject(command: RequestUnassignEmployeeFromProjectCommand): Employee {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(command.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${command.employeeUuid}] not found")

        employee.requestUnassignProject(command.projectUuid)

        return employeeRepository.save(employee).apply { storeEvent(registeredEvents()) }
    }
}