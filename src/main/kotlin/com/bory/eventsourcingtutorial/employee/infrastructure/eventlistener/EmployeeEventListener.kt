package com.bory.eventsourcingtutorial.employee.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.department.application.event.EmployeeMoveAcceptedEvent
import com.bory.eventsourcingtutorial.department.application.event.EmployeeMoveFailedEvent
import com.bory.eventsourcingtutorial.department.application.event.EmployeeReleasedEvent
import com.bory.eventsourcingtutorial.employee.domain.exception.NoSuchEmployeeException
import com.bory.eventsourcingtutorial.employee.infrastructure.persistence.EmployeeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class EmployeeEventListener(
    private val employeeRepository: EmployeeRepository
) {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(EmployeeEventListener::class.java)
    }

    @Async
    @EventListener
    fun on(event: EmployeeMoveFailedEvent) {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid} not found.")

        LOGGER.info("Employee[${event.employeeUuid}] MOVE TO Department[${event.toDepartmentUuid}] FAILED: ${event.reason}")

        if (event.fromDepartmentUuid != null) {
            employee.moveToDepartmentFailed(event.fromDepartmentUuid)
            employeeRepository.save(employee)
        } else {
            LOGGER.error("FAILED TO CREATE EMPLOYEE!!! $employee")
            employeeRepository.delete(employee)
        }

    }

    @Async
    @EventListener
    fun on(event: EmployeeMoveAcceptedEvent) {
        val employee = employeeRepository.findByUuidAndDeletedIsFalse(event.employeeUuid)
            ?: throw NoSuchEmployeeException("Employee uuid[${event.employeeUuid} not found.")

        employee.movetoDepartmentAccepted(event.acceptedDepartmentUuid)

        employeeRepository.save(employee)
    }

    @Async
    @EventListener
    fun on(event: EmployeeReleasedEvent) {
        LOGGER.debug("Employee[${event.employeeUuid}] RELEASED FROM Department[${event.releasedDepartmentUuid}] FINISHED.")
    }
}