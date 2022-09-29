package com.bory.eventsourcingtutorial.employee.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.core.infrastructure.extensions.retry
import com.bory.eventsourcingtutorial.employee.application.event.*
import com.bory.eventsourcingtutorial.employee.domain.exception.NoSuchEmployeeException
import com.bory.eventsourcingtutorial.employee.domain.exception.ProjectNotAssignedException
import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class EmployeeEventListener(
    private val employeeRepository: EmployeeRepository
) {
    @Async
    @EventListener(classes = [EmployeeCreatedEvent::class])
    fun on(event: EmployeeCreatedEvent) {
        retry { employeeRepository.save(event.employee) }
    }

    @Async
    @EventListener(classes = [EmployeeUpdatedEvent::class])
    fun on(event: EmployeeUpdatedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchEmployeeException::class.java)) {
            val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employee.uuid)
                ?: throw NoSuchEmployeeException("Employee uuid[${event.employee.uuid}] not found")

            employee.updateWith(event.employee)

            employeeRepository.save(employee)
        }
    }

    @Async
    @EventListener(classes = [EmployeeDeletedEvent::class])
    fun on(event: EmployeeDeletedEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchEmployeeException::class.java)) {
            val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
                ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid}] not found")

            employee.delete()

            employeeRepository.save(employee)
        }
    }

    @Async
    @EventListener(classes = [EmployeeAssignedToProjectEvent::class])
    fun on(event: EmployeeAssignedToProjectEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchEmployeeException::class.java)) {
            val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
                ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid}] not found")

            employee.assignProject(event.projectUuid)

            employeeRepository.save(employee)
        }
    }

    @Async
    @EventListener(classes = [EmployeeUnassignedFromProjectEvent::class])
    fun on(event: EmployeeUnassignedFromProjectEvent) {
        retry(
            skipRetryExceptions = arrayOf(
                NoSuchEmployeeException::class.java,
                ProjectNotAssignedException::class.java
            )
        ) {
            val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
                ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid}] not found")

            employee.unassignProject(event.projectUuid)

            employeeRepository.save(employee)
        }
    }

    @Async
    @EventListener(classes = [EmployeeMovedToDepartmentEvent::class])
    fun on(event: EmployeeMovedToDepartmentEvent) {
        retry(skipRetryExceptions = arrayOf(NoSuchEmployeeException::class.java)) {
            val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
                ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid}] not found")

            employee.moveToDepartment(event.departmentUuid)

            employeeRepository.save(employee)
        }
    }
}