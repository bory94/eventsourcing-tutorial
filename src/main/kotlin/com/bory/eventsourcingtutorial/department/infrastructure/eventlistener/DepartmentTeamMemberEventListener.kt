package com.bory.eventsourcingtutorial.department.infrastructure.eventlistener

import com.bory.eventsourcingtutorial.core.infrastructure.annotations.DomainEventListener
import com.bory.eventsourcingtutorial.department.application.event.EmployeeMoveFailedEvent
import com.bory.eventsourcingtutorial.department.domain.DepartmentTeamMember
import com.bory.eventsourcingtutorial.department.domain.exception.NoSuchDepartmentException
import com.bory.eventsourcingtutorial.department.infrastructure.persistence.DepartmentRepository
import com.bory.eventsourcingtutorial.employee.application.event.EmployeeMoveRequestedToDepartmentEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

@Transactional
@DomainEventListener
class DepartmentTeamMemberEventListener(
    private val departmentRepository: DepartmentRepository
) {
    @Async
    @EventListener
    fun on(event: EmployeeMoveRequestedToDepartmentEvent): Any {
        try {
            val toDepartment =
                departmentRepository.findByUuidAndDeletedIsFalse(event.toDepartmentUuid)
                    ?: throw NoSuchDepartmentException("To Department uuid[${event.toDepartmentUuid}] not found.")

            toDepartment.tryAcceptTeamMember(
                DepartmentTeamMember(
                    departmentUuid = event.toDepartmentUuid,
                    employeeUuid = event.employeeUuid
                )
            )
            departmentRepository.save(toDepartment)

            if (event.fromDepartmentUuid != null) {
                val fromDepartment =
                    departmentRepository.findByUuidAndDeletedIsFalse(event.fromDepartmentUuid)
                        ?: throw NoSuchDepartmentException("From Department uuid[${event.fromDepartmentUuid}] not found.")
                fromDepartment.tryReleaseTeamMember(event.employeeUuid)
                departmentRepository.save(fromDepartment)
            }
        } catch (e: Exception) {
            return EmployeeMoveFailedEvent(event, e.message ?: "Unknown Reason")
        }

        return Unit
    }
}